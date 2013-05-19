package kikuyu.view

import com.vaadin.ui.*
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class EditPageViewTest {

    private Page page
    private GrailsMock mockForPresenter
    private EditPageView target
    private KikuyuPresenter presenter

    @Before
    public void setUp() throws Exception {
        page = new Page()
        page.name = "test name"
        page.pageComponents = []
        page.pageComponents.add(new PageComponent(url: "test component url1"))
        page.pageComponents.add(new PageComponent(url: "test component url2"))

        mockForPresenter = mockFor(KikuyuPresenter)
        presenter = mockForPresenter.createMock()
        target = new EditPageView(presenter, page)
    }

//    todo: needs more tests
    void testInit() {
        int j = 0
        final Component layout = target.getComponent(j++)

        int i = 0
        final Component nameField = layout.getComponent(i++)
        assert nameField instanceof TextField
        assert nameField.caption == "Name"
        assert nameField.value == "test name"

        final GridLayout gridLayout = layout.getComponent(i++)
        assert gridLayout instanceof Layout

        assert gridLayout.getComponent(0, 0).caption == "Component URL"
        assert gridLayout.getComponent(0, 0).value == "test component url1"
        assert gridLayout.getComponent(1, 0) instanceof Button
        assert gridLayout.getComponent(1, 0).icon.resourceId == "minus_sign.png"

        final Component gridLayout2 = layout.getComponent(i++)
        assert gridLayout2 instanceof GridLayout
        assert gridLayout2.getComponent(0, 0) instanceof TextField
        assert gridLayout2.getComponent(0, 0).caption == "Component URL"
        assert gridLayout2.getComponent(0, 0).value == "test component url2"
        assert gridLayout2.getComponent(1, 0) instanceof Button
        assert gridLayout2.getComponent(1, 0).icon.resourceId == "minus_sign.png"

        final Component addButton = target.getComponent(j++)
        assert addButton instanceof Button
        assert addButton.caption == "add component"

        final Component button = target.getComponent(j++)
        assert button instanceof Button
        assert button.caption == "done"
    }
}
