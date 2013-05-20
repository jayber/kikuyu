package kikuyu.view

import com.vaadin.ui.Button
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
        mock.demand.getPageTableEventAction() {
            return {
                println "nothing"
            }
        }
        mock.demand.getCreateNewPage() {
            return {
                println "nothing"
            }
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
        final layout = tab.component
        final table = layout.getComponent(0)
        assert table instanceof Table

        final button = layout.getComponent(1)
        assert button instanceof Button
        assert button.caption == "new"
    }

    private void doUrlMappingTabTests(TabSheet.Tab tab) {
        assert tab.caption == "[default.urlMappingTab.label]"

        final layout = tab.component
        final table = layout.getComponent(0)
        assert table instanceof Table
        assert table.itemIds == rows

        assert table.visibleColumns == ["pattern", "page", "matchOrder"]

        final button = layout.getComponent(1)
        assert button instanceof Button
        assert button.caption == "new"
    }

    @After
    public void tearDown() throws Exception {
        mock.verify()
    }
}
