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
package net.hedtech.restfulapi.config

import net.hedtech.restfulapi.extractors.xml.DeclarativeXMLExtractor

/**
 * A factory for creating DeclarativeXMLExtractor instances
 * from configuration.
 */
class XMLExtractorFactory {

    static DeclarativeXMLExtractor instantiate(XMLExtractorConfig config, RestConfig restConfig) {
        //Merge the include chain into a final config
        config = restConfig.xmlExtractor.getMergedConfig( config )

        def extractor = new DeclarativeXMLExtractor()
        extractor.dottedRenamedPaths.putAll config.dottedRenamedPaths
        extractor.dottedValuePaths.putAll config.dottedValuePaths
        extractor.dottedShortObjectPaths.addAll config.dottedShortObjectPaths
        extractor.dottedFlattenedPaths.addAll config.dottedFlattenedPaths
        if (config.isShortObjectClosureSet) extractor.shortObjectClosure = config.shortObjectClosure
        extractor.dottedDatePaths = config.dottedDatePaths
        extractor.dateFormats = config.dateFormats

        extractor
    }

}
