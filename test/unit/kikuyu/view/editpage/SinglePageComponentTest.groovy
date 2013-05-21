package kikuyu.view.editpage

import com.vaadin.ui.Layout
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import org.junit.Before
import org.junit.Test

@TestMixin(GrailsUnitTestMixin)
class SinglePageComponentTest {
    private SinglePageComponent target
    private GrailsMock mockControlContainer
    private GrailsMock mockControlLayout
    private PageComponent pageComponent
    private Page page
    private GrailsMock mockControlPresenter

    @Before
    public void setUp() throws Exception {
        pageComponent = new PageComponent(url: "testUrl", substitutionVariables: [var1: "value1", var2: "value2"])
        page = new Page(pageComponents: [pageComponent])

        mockControlLayout = mockFor(Layout)
        mockControlPresenter = mockFor(KikuyuPresenter)
        mockControlContainer = mockFor(EditPageView)

    }

    @Test
    public void testInit() throws Exception {

        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())

        def layout = target.getComponent(0)

        def urlField = layout.getComponent(0, 0)
        assert urlField.value == "testUrl"

        def delButton = layout.getComponent(1, 0)
        assert delButton.icon.resourceId == "minus_sign.png"

        def templateBox = layout.getComponent(2, 0)
        assert templateBox.caption == "Accept POSTs?"

        def postBox = layout.getComponent(3, 0)
        assert postBox.caption == "Template?"

        def innerLayout = layout.getComponent(0, 1)
        def scan = innerLayout.getComponent(0)
        assert scan.caption == "scan"

        def substLayout = innerLayout.getComponent(1)
        def varLabel = substLayout.getComponent(0)
        assert varLabel.value == "Variables"
    }
}
