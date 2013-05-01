package kikuyu.domain

import org.springframework.dao.DataIntegrityViolationException

class FolderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [folderInstanceList: Folder.list(params), folderInstanceTotal: Folder.count()]
    }

    def create() {
        [folderInstance: new Folder(params)]
    }

    def save() {
        def folderInstance = new Folder(params)
        if (!folderInstance.save(flush: true)) {
            render(view: "create", model: [folderInstance: folderInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'folder.label', default: 'Folder'), folderInstance.id])
        redirect(action: "show", id: folderInstance.id)
    }

    def show(Long id) {
        def folderInstance = Folder.get(id)
        if (!folderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'folder.label', default: 'Folder'), id])
            redirect(action: "list")
            return
        }

        [folderInstance: folderInstance]
    }

    def edit(Long id) {
        def folderInstance = Folder.get(id)
        if (!folderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'folder.label', default: 'Folder'), id])
            redirect(action: "list")
            return
        }

        [folderInstance: folderInstance]
    }

    def update(Long id, Long version) {
        def folderInstance = Folder.get(id)
        if (!folderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'folder.label', default: 'Folder'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (folderInstance.version > version) {
                folderInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'folder.label', default: 'Folder')] as Object[],
                        "Another user has updated this Folder while you were editing")
                render(view: "edit", model: [folderInstance: folderInstance])
                return
            }
        }

        folderInstance.properties = params

        if (!folderInstance.save(flush: true)) {
            render(view: "edit", model: [folderInstance: folderInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'folder.label', default: 'Folder'), folderInstance.id])
        redirect(action: "show", id: folderInstance.id)
    }

    def delete(Long id) {
        def folderInstance = Folder.get(id)
        if (!folderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'folder.label', default: 'Folder'), id])
            redirect(action: "list")
            return
        }

        try {
            folderInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'folder.label', default: 'Folder'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'folder.label', default: 'Folder'), id])
            redirect(action: "show", id: id)
        }
    }
}
