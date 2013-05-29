package kikuyu.view.editpage

import com.vaadin.ui.Layout
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class SinglePageComponentTest {
    private SinglePageComponent target
    private GrailsMock mockControlContainer
    private GrailsMock mockControlLayout
    private PageComponent pageComponent
    private GrailsMock mockControlPresenter

    @Before
    public void setUp() throws Exception {
        pageComponent = new PageComponent(url: "testUrl", substitutionVariables: [var1: "value1", var2: "value2"])

        mockControlLayout = mockFor(Layout)
        mockControlPresenter = mockFor(KikuyuPresenter)
        mockControlContainer = mockFor(EditPageView)

    }

    public void testInit() throws Exception {
        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())
        def layout = target.getComponent(0)

        def urlField = layout.getComponent(0, 0)
        assert urlField.value == "testUrl"

        def delButton = layout.getComponent(2, 0)
        assert delButton.icon.resourceId == "minus_sign.png"

        def postBox = layout.getComponent(3, 0)
        assert postBox.caption == "Accept POSTs?"

        def templateBox = layout.getComponent(4, 0)
        assert templateBox.caption == "Template?"

        def innerLayout = layout.getComponent(0, 1)
        def scan = innerLayout.getComponent(0)
        assert scan.caption == "scan"

        def varLayout = innerLayout.getComponent(1)
        def addVarButton = varLayout.getComponent(0)
        assert addVarButton.caption == "add variable"

        def substLayout = varLayout.getComponent(1)
        def varLabel = substLayout.getComponent(0)
        assert varLabel.value == "Variables"

        def hlayout1 = substLayout.getComponent(1)
        def varField1 = hlayout1.getComponent(0)
        assert varField1.caption == "var1"
        assert varField1.value == "value1"

        def hlayout2 = substLayout.getComponent(2)
        def varField2 = hlayout2.getComponent(0)
        assert varField2.caption == "var2"
        assert varField2.value == "value2"
    }

    public void testRemoveVariable() throws Exception {

        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())
        target.removeVariable("var1")
        assertFalse target.pageComponent.substitutionVariables.containsKey("var1")

        def component = target.getComponent(0)?.getComponent(0, 1)?.getComponent(1)?.getComponent(1)?.getComponent(1)?.getComponent(0)
        assertFalse component.caption == "var1"
    }

    public void testGetUrl() throws Exception {
        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())
        assert "testUrl" == target.getUrl()
    }

    public void testSetSlots() throws Exception {
        mockControlPresenter.demand.savePage(1) { Page page -> }
        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())

        target.setSlots(4)

        assert 4 == target.pageComponent.slots
        def component = target.templateBox
        assert component.value
    }

    public void testSetSubstitutionVariables() throws Exception {
        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())

        Map vars = [newVar1: "newVal1", newVar2: "newVal2"]
        target.setSubstitutionVariables(vars)

        assert vars == target.pageComponent.substitutionVariables
        def component = target.getComponent(0)?.getComponent(0, 1)?.getComponent(1)?.getComponent(1)?.getComponent(1)?.getComponent(0)
        assert "newVar1" == component.caption
    }

    public void testAddSubstitutionVariable() throws Exception {
        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())

        target.addSubstitutionVariable("newVar1")

        assert target.pageComponent.substitutionVariables.containsKey("newVar1")
        def component = target.getComponent(0)?.getComponent(0, 1)?.getComponent(1)?.getComponent(1)?.getComponent(3)?.getComponent(0)
        assert "newVar1" == component.caption
    }
}
