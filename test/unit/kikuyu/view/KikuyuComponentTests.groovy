package kikuyu.view

import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Layout
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.Runo
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.After
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class KikuyuComponentTests {

    KikuyuComponent component
    GrailsMock mock


    @Before
    void setUp() {
        mock = mockFor(KikuyuPresenter)

        mock.demand.buildNavigator() { VerticalLayout layout, UI ui -> }

        def uiMock = {
            getPage: {
                [:] as com.vaadin.server.Page
            }
        } as UI

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
        assert body.margin == new MarginInfo(false, false, false, false)
        assert !body.spacing

    }

    private doHeaderTests() {
        final titleBar = component.getComponent(0)
        assert titleBar.margin == new MarginInfo(true, true, true, true)
        assert component.getExpandRatio(titleBar) == 0
        final title = titleBar.getComponent(0)
        assert title.value == "[default.home.label]"
        assert title.styleName == "$Runo.LABEL_H1 kikuyu-header-label"


        final description = titleBar.getComponent(1)
        assert description.value == "[default.description.label]"
    }

    @After
    public void tearDown() throws Exception {
        mock.verify()
    }
}
