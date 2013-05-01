package kikuyu.domain

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(SavedItemsController)
@Mock(SavedItem)
class SavedItemsControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/savedItems/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.savedItemsInstanceList.size() == 0
        assert model.savedItemsInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.savedItemsInstance != null
    }

    void testSave() {
        controller.save()

        assert model.savedItemsInstance != null
        assert view == '/savedItems/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/savedItems/show/1'
        assert controller.flash.message != null
        assert SavedItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/savedItems/list'

        populateValidParams(params)
        def savedItems = new SavedItem(params)

        assert savedItems.save() != null

        params.id = savedItems.id

        def model = controller.show()

        assert model.savedItemsInstance == savedItems
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/savedItems/list'

        populateValidParams(params)
        def savedItems = new SavedItem(params)

        assert savedItems.save() != null

        params.id = savedItems.id

        def model = controller.edit()

        assert model.savedItemsInstance == savedItems
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/savedItems/list'

        response.reset()

        populateValidParams(params)
        def savedItems = new SavedItem(params)

        assert savedItems.save() != null

        // test invalid parameters in update
        params.id = savedItems.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/savedItems/edit"
        assert model.savedItemsInstance != null

        savedItems.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/savedItems/show/$savedItems.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        savedItems.clearErrors()

        populateValidParams(params)
        params.id = savedItems.id
        params.version = -1
        controller.update()

        assert view == "/savedItems/edit"
        assert model.savedItemsInstance != null
        assert model.savedItemsInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/savedItems/list'

        response.reset()

        populateValidParams(params)
        def savedItems = new SavedItem(params)

        assert savedItems.save() != null
        assert SavedItem.count() == 1

        params.id = savedItems.id

        controller.delete()

        assert SavedItem.count() == 0
        assert SavedItem.get(savedItems.id) == null
        assert response.redirectedUrl == '/savedItems/list'
    }
}
