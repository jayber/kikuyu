package kikuyu.view

import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Layout
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Table
import com.vaadin.ui.UI
import com.vaadin.ui.themes.Runo
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class KikuyuComponentTests {

    KikuyuComponent component
    GrailsMock mock
    ArrayList<UrlMapping> rows
    ArrayList<Page> pageRows


    @Before
    void setUp() {
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
        def uiMock = [getPage: {
            [:] as Page
        }] as UI

        component = new KikuyuComponent(mock.createMock(), uiMock)

    }

    void testComponentTree() {

        assert component.margin == new MarginInfo(false, false, false, false)
        assert component.spacing

        doHeaderTests()

        doBodyTests()
    }

    private doBodyTests() {
        final Layout body = component.getComponent(1)
        assert body.margin == new MarginInfo(true, true, true, true)
        assert body.spacing
        assert component.getExpandRatio(body) == 1
        final description = body.getComponent(0)
        assert description.value == "[default.description.label]"

        doTabsTests(body.getComponent(1))
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

    private doHeaderTests() {
        final titleBar = component.getComponent(0)
        assert titleBar.margin == new MarginInfo(true, true, true, true)
        assert component.getExpandRatio(titleBar) == 0
        final title = titleBar.getComponent(0)
        assert title.value == "[default.home.label]"
        assert title.styleName == Runo.LABEL_H1
    }
}
