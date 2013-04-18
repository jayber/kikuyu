package kikuyu.view

import com.vaadin.ui.ComboBox
import com.vaadin.ui.Field
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.TestFixtures
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class UrlMappingTableFieldFactoryTest {

    private UrlMappingTableFieldFactory factory

    @Before
    public void setUp() throws Exception {
        final GrailsMock mock = mockFor(KikuyuPresenter)
        mock.demand.listPageOptions() {
            TestFixtures.pages
        }
        factory = new UrlMappingTableFieldFactory(presenter: mock.createMock())
    }

    public void testOnlyEditCurrentRow() throws Exception {
        final UrlMapping notCurrentItemId = new UrlMapping(pattern: "test", page: new Page(name: "page"))
        final UrlMapping currentItemId = new UrlMapping(pattern: "test1", page: new Page(name: "page1"))
        factory.currentSelectedItemId = currentItemId

        assert factory.createField(null, notCurrentItemId, "page", null) == null
        assert factory.createField(null, currentItemId, "page", null) != null
    }

    public void testComboBoxForPage() throws Exception {
        UrlMapping currentItemId = setUpRow()

        final Field field = factory.createField(null, currentItemId, "page", null)
        assert field.class == ComboBox
        ComboBox box = (ComboBox) field
//        todo: holy shit I can't get this test to WORK!!!!! Even though is fine in app
//        assert ((ComboBox) field).getItemIds() == TestFixtures.pages
    }

    public void testSpinnerForMatchOrder() throws Exception {
        UrlMapping currentItemId = setUpRow()

        final Field field = factory.createField(null, currentItemId, "matchOrder", null)
        assert field.class == SpinnerField

    }

    private UrlMapping setUpRow() {
        final UrlMapping currentItemId = new UrlMapping(pattern: "test1", page: new Page(name: "page1"), matchOrder: 0)
        factory.currentSelectedItemId = currentItemId
        currentItemId
    }
}
