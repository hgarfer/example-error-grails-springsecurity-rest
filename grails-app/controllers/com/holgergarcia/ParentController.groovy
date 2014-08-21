package com.holgergarcia


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ParentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Parent.list(params), model: [parentInstanceCount: Parent.count()]
    }

    def show(Parent parentInstance) {
        respond parentInstance
    }

    def create() {
        respond new Parent(params)
    }

    @Transactional
    def save(Parent parentInstance) {
        if (parentInstance == null) {
            notFound()
            return
        }

        if (parentInstance.hasErrors()) {
            respond parentInstance.errors, view: 'create'
            return
        }

        parentInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'parent.label', default: 'Parent'), parentInstance.id])
                redirect parentInstance
            }
            '*' { respond parentInstance, [status: CREATED] }
        }
    }

    def edit(Parent parentInstance) {
        respond parentInstance
    }

    @Transactional
    def update(Parent parentInstance) {
        if (parentInstance == null) {
            notFound()
            return
        }

        if (parentInstance.hasErrors()) {
            respond parentInstance.errors, view: 'edit'
            return
        }

        parentInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Parent.label', default: 'Parent'), parentInstance.id])
                redirect parentInstance
            }
            '*' { respond parentInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Parent parentInstance) {

        if (parentInstance == null) {
            notFound()
            return
        }

        parentInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Parent.label', default: 'Parent'), parentInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'parent.label', default: 'Parent'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
