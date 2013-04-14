package kikuyu.view

import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Layout
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Table
import com.vaadin.ui.themes.Runo
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.UrlMapping
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class KikuyuComponentTests {

    KikuyuComponent component
    GrailsMock mock
    ArrayList<UrlMapping> rows

    @Before
    void setUp() {
        mock = mockFor(KikuyuPresenter)
        mock.demand.getTableDataSource() {
            rows = [
                    new UrlMapping(pattern: "a", matchOrder: 1),
                    new UrlMapping(pattern: "b", matchOrder: 0)
            ]
            new NamedColumnContainer<UrlMapping>(rows, UrlMapping, "pattern", "page", "matchOrder")
        }
        component = new KikuyuComponent(mock.createMock())
    }

    void testComponentTree() {
        assert component.margin == new MarginInfo(true, true, true, true)
        assert component.spacing

        doHeaderTests()

        doBodyTests()
    }

    private doBodyTests() {
        final Layout body = component.getComponent(1)
        assert body.spacing
        assert component.getExpandRatio(body) == 1
        final description = body.getComponent(0)
        assert description.value == "[default.description.label]"

        doUrlMappingTabTests(body.getComponent(1))
    }

    private doUrlMappingTabTests(TabSheet tabs) {
        assert tabs.getComponentCount() == 1
        final tab = tabs.getTab(0)
        assert tab.caption == "[default.urlMappingTab.label]"

        final table = tab.component
        assert table instanceof Table
        assert table.itemIds == rows

        assert table.visibleColumns == ["pattern", "page", "matchOrder"]
    }

    private doHeaderTests() {
        final titleBar = component.getComponent(0)
        assert component.getExpandRatio(titleBar) == 0
        final title = titleBar.getComponent(0)
        assert title.value == "[default.home.label]"
        assert title.styleName == Runo.LABEL_H1
    }
}
