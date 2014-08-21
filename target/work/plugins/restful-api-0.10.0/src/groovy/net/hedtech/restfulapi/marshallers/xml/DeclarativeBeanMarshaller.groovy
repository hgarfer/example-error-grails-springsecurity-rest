/* ****************************************************************************
 * Copyright 2013 Ellucian Company L.P. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package net.hedtech.restfulapi.marshallers.xml

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import grails.converters.XML
import groovy.lang.GroovyObject

import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

import org.codehaus.groovy.grails.support.proxy.ProxyHandler

import org.springframework.beans.BeanWrapper

class DeclarativeBeanMarshaller extends BeanMarshaller {

    protected static final Log log =
        LogFactory.getLog(DeclarativeBeanMarshaller.class)

    Class supportClass
    String elementName
    def fieldNames = [:]
    def excludedFields = []
    def includedFields = null
    boolean requireIncludedFields = false
    def additionalFieldClosures = []
    def additionalFieldsMap = [:]
    //If a field is a key in the map, then represents
    //a field-level override for whether to marshall that
    //field if null.  Otherise, includeNullFields is used
    //to determine whether to marshall a field if null
    def marshalledNullFields = [:]
    //default behavior on whether to marshall null fields.
    def marshallNullFields = true


    @Override
    String getElementName(Object o) {
        if (elementName != null) {
            elementName
        } else {
            super.getElementName(o)
        }
    }

    @Override
    public boolean supports(Object object) {
        if (supportClass) {
            supportClass.isInstance(object)
        } else {
            super.supports(object)
        }
    }

    @Override
    protected List<String> getExcludedFields(Object value) {
        return excludedFields
    }

    @Override
    protected List<String> getIncludedFields(Object value) {
        return includedFields
    }

    @Override
    protected boolean requireIncludedFields(Object o) {
        return this.requireIncludedFields
    }

    @Override
    protected String getSubstitutionName(BeanWrapper beanWrapper, PropertyDescriptor property) {
        return fieldNames.get( property.getName() )
    }

    @Override
    protected String getSubstitutionName(Object value, Field field) {
        return fieldNames.get( field.getName() )
    }

    @Override
    protected boolean processProperty(BeanWrapper beanWrapper,
                                      PropertyDescriptor property,
                                      XML xml) {
        boolean ignoreNull = false
        if (marshalledNullFields.containsKey(property.getName())) {
            ignoreNull = !marshalledNullFields[property.getName()]
        } else {
            ignoreNull = !marshallNullFields
        }

        if (ignoreNull) {
            Object val = beanWrapper.getPropertyValue(property.getName())
            return val != null
        } else {
            return true
        }
    }

    @Override
    protected boolean processField(Object value,
                                   Field field,
                                   XML xml) {
        boolean ignoreNull = false
        if (marshalledNullFields.containsKey(field.getName())) {
            ignoreNull = !marshalledNullFields[field.getName()]
        } else {
            ignoreNull = !marshallNullFields
        }

        if (ignoreNull) {
            Object val = field.get(value)
            return val != null
        } else {
            return true
        }
    }

    @Override
    protected void processAdditionalFields(BeanWrapper beanWrapper, XML xml) {
        Map map = [:]
        map.putAll( additionalFieldsMap )
        map.putAll(
            [
                'grailsApplication':app,
                'beanWrapper':beanWrapper,
                'xml':xml
            ]
        )
        if (!map['resourceName']) {
            map['resourceName'] = getDerivedResourceName(beanWrapper)
        }
        //see if we have something that looks like an id property or field
        //metaprogrammed
        def id = null
        def val = beanWrapper.getWrappedInstance()
        if (val != null) {
            if (val.metaClass && val.metaClass.hasProperty(val, 'id')) {
                id = val.'id'
            } else if (val.metaClass && val.metaClass.respondsTo(val, 'getId', new Object[0])) {
                id = val.getId()
            } else if (getAvailableProperties(beanWrapper)*.name.contains('id')) {
                id = beanWrapper.getPropertyValue('id')
            } else if (getAvailableFields(beanWrapper.getWrappedInstance())*.name.contains('id')) {
                id = val.id
            }
            if (id != null) {
                map['resourceId'] = id
            }
        }
        //map['resourceId'] = beanWrapper.getPropertyValue(domainClass.getIdentifier().getName())
        additionalFieldClosures.each { c ->
            c.call( map )
        }
    }

}
