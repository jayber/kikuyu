package kikuyu.view

import com.vaadin.ui.TabSheet
import com.vaadin.ui.Table
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import org.junit.After
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class DataTablesViewTest {
    private DataTablesView component
    ArrayList<UrlMapping> rows
    ArrayList<Page> pageRows

    private GrailsMock mock

    @Before
    public void setUp() throws Exception {
        mock = mockFor(KikuyuPresenter)
        mock.demand.getUrlMappingTableDataSource() {
            rows = [
                    new UrlMapping(1, "a"),
                    new UrlMapping(0, "b")
            ]
            new NamedColumnContainer<UrlMapping>(rows, UrlMapping, "pattern", "page", "matchOrder")
        }
        mock.demand.getPageTableDataSource() {
            pageRows = [
                    new Page(name: "a"),
                    new Page(name: "b")
            ]
            new NamedColumnContainer<UrlMapping>(rows, UrlMapping, "name")
        }
        component = new DataTablesView(mock.createMock())
    }

    public void testConstruction() throws Exception {

        doTabsTests(component.getComponent(0))

    }

    private doTabsTests(TabSheet tabs) {
        assert tabs.getComponentCount() == 2
        doUrlMappingTabTests(tabs.getTab(0))
        doPageTabTests(tabs.getTab(1))
    }

    private doPageTabTests(TabSheet.Tab tab) {
        assert tab.caption == "[default.pageTab.label]"
        final table = tab.component
        assert table instanceof Table
    }

    private void doUrlMappingTabTests(TabSheet.Tab tab) {
        assert tab.caption == "[default.urlMappingTab.label]"

        final table = tab.component
        assert table instanceof Table
        assert table.itemIds == rows

        assert table.visibleColumns == ["pattern", "page", "matchOrder"]
    }

    @After
    public void tearDown() throws Exception {
        mock.verify()
    }
}
