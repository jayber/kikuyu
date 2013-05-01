package kikuyu

import kikuyu.domain.SavedItem
import org.springframework.dao.DataIntegrityViolationException

class SavedItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [savedItemInstanceList: SavedItem.list(params), savedItemInstanceTotal: SavedItem.count()]
    }

    def create() {
        [savedItemInstance: new SavedItem(params)]
    }

    def save() {
        def savedItemInstance = new SavedItem(params)
        if (!savedItemInstance.save(flush: true)) {
            render(view: "create", model: [savedItemInstance: savedItemInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), savedItemInstance.id])
        redirect(action: "show", id: savedItemInstance.id)
    }

    def show(Long id) {
        def savedItemInstance = SavedItem.get(id)
        if (!savedItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), id])
            redirect(action: "list")
            return
        }

        [savedItemInstance: savedItemInstance]
    }

    def edit(Long id) {
        def savedItemInstance = SavedItem.get(id)
        if (!savedItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), id])
            redirect(action: "list")
            return
        }

        [savedItemInstance: savedItemInstance]
    }

    def update(Long id, Long version) {
        def savedItemInstance = SavedItem.get(id)
        if (!savedItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (savedItemInstance.version > version) {
                savedItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'savedItem.label', default: 'SavedItem')] as Object[],
                        "Another user has updated this SavedItem while you were editing")
                render(view: "edit", model: [savedItemInstance: savedItemInstance])
                return
            }
        }

        savedItemInstance.properties = params

        if (!savedItemInstance.save(flush: true)) {
            render(view: "edit", model: [savedItemInstance: savedItemInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), savedItemInstance.id])
        redirect(action: "show", id: savedItemInstance.id)
    }

    def delete(Long id) {
        def savedItemInstance = SavedItem.get(id)
        if (!savedItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), id])
            redirect(action: "list")
            return
        }

        try {
            savedItemInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'savedItem.label', default: 'SavedItem'), id])
            redirect(action: "show", id: id)
        }
    }
}
