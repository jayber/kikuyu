package kikuyu.domain

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(FolderController)
@Mock(Folder)
class FolderControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/folder/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.folderInstanceList.size() == 0
        assert model.folderInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.folderInstance != null
    }

    void testSave() {
        controller.save()

        assert model.folderInstance != null
        assert view == '/folder/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/folder/show/1'
        assert controller.flash.message != null
        assert Folder.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/folder/list'

        populateValidParams(params)
        def folder = new Folder(params)

        assert folder.save() != null

        params.id = folder.id

        def model = controller.show()

        assert model.folderInstance == folder
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/folder/list'

        populateValidParams(params)
        def folder = new Folder(params)

        assert folder.save() != null

        params.id = folder.id

        def model = controller.edit()

        assert model.folderInstance == folder
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/folder/list'

        response.reset()

        populateValidParams(params)
        def folder = new Folder(params)

        assert folder.save() != null

        // test invalid parameters in update
        params.id = folder.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/folder/edit"
        assert model.folderInstance != null

        folder.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/folder/show/$folder.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        folder.clearErrors()

        populateValidParams(params)
        params.id = folder.id
        params.version = -1
        controller.update()

        assert view == "/folder/edit"
        assert model.folderInstance != null
        assert model.folderInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/folder/list'

        response.reset()

        populateValidParams(params)
        def folder = new Folder(params)

        assert folder.save() != null
        assert Folder.count() == 1

        params.id = folder.id

        controller.delete()

        assert Folder.count() == 0
        assert Folder.get(folder.id) == null
        assert response.redirectedUrl == '/folder/list'
    }
}
