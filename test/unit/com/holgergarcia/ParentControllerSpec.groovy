package com.holgergarcia


import grails.test.mixin.*
import spock.lang.*

@TestFor(ParentController)
@Mock(Parent)
class ParentControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.parentInstanceList
        model.parentInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.parentInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        def parent = new Parent()
        parent.validate()
        controller.save(parent)

        then: "The create view is rendered again with the correct model"
        model.parentInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        parent = new Parent(params)

        controller.save(parent)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/parent/show/1'
        controller.flash.message != null
        Parent.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def parent = new Parent(params)
        controller.show(parent)

        then: "A model is populated containing the domain instance"
        model.parentInstance == parent
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def parent = new Parent(params)
        controller.edit(parent)

        then: "A model is populated containing the domain instance"
        model.parentInstance == parent
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/parent/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def parent = new Parent()
        parent.validate()
        controller.update(parent)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.parentInstance == parent

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        parent = new Parent(params).save(flush: true)
        controller.update(parent)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/parent/show/$parent.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/parent/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def parent = new Parent(params).save(flush: true)

        then: "It exists"
        Parent.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(parent)

        then: "The instance is deleted"
        Parent.count() == 0
        response.redirectedUrl == '/parent/index'
        flash.message != null
    }
}
