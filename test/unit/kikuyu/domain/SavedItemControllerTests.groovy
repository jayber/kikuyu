package kikuyu.domain

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kikuyu.SavedItemController

@TestFor(SavedItemController)
@Mock(SavedItem)
class SavedItemControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/savedItem/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.savedItemInstanceList.size() == 0
        assert model.savedItemInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.savedItemInstance != null
    }

    void testSave() {
        controller.save()

        assert model.savedItemInstance != null
        assert view == '/savedItem/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/savedItem/show/1'
        assert controller.flash.message != null
        assert SavedItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/savedItem/list'

        populateValidParams(params)
        def savedItem = new SavedItem(params)

        assert savedItem.save() != null

        params.id = savedItem.id

        def model = controller.show()

        assert model.savedItemInstance == savedItem
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/savedItem/list'

        populateValidParams(params)
        def savedItem = new SavedItem(params)

        assert savedItem.save() != null

        params.id = savedItem.id

        def model = controller.edit()

        assert model.savedItemInstance == savedItem
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/savedItem/list'

        response.reset()

        populateValidParams(params)
        def savedItem = new SavedItem(params)

        assert savedItem.save() != null

        // test invalid parameters in update
        params.id = savedItem.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/savedItem/edit"
        assert model.savedItemInstance != null

        savedItem.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/savedItem/show/$savedItem.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        savedItem.clearErrors()

        populateValidParams(params)
        params.id = savedItem.id
        params.version = -1
        controller.update()

        assert view == "/savedItem/edit"
        assert model.savedItemInstance != null
        assert model.savedItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/savedItem/list'

        response.reset()

        populateValidParams(params)
        def savedItem = new SavedItem(params)

        assert savedItem.save() != null
        assert SavedItem.count() == 1

        params.id = savedItem.id

        controller.delete()

        assert SavedItem.count() == 0
        assert SavedItem.get(savedItem.id) == null
        assert response.redirectedUrl == '/savedItem/list'
    }
}
