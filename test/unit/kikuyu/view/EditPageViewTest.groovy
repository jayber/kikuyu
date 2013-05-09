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

    void testEnter() {
        int j = 0
        final Component layout = target.getComponent(j++)

        int i = 0
        final Component nameField = layout.getComponent(i++)
        assert nameField instanceof TextField
        assert nameField.caption == "Name"
        assert nameField.value == "test name"

        final GridLayout componentLayout = layout.getComponent(i++)
        assert componentLayout instanceof Layout

        assert componentLayout.getComponent(0, 0).caption == "Component URL"
        assert componentLayout.getComponent(0, 0).value == "test component url1"
        assert componentLayout.getComponent(0, 1) instanceof Button
        assert componentLayout.getComponent(0, 1).icon.resourceId == "minus_sign.png"

        final Component componentLayout2 = layout.getComponent(i++)
        assert componentLayout2 instanceof VerticalLayout
        final Component urlFieldLayout2 = componentLayout2.getComponent(0)
        assert urlFieldLayout instanceof HorizontalLayout
        assert urlFieldLayout2.getComponent(0) instanceof TextField
        assert urlFieldLayout2.getComponent(0).caption == "Component URL"
        assert urlFieldLayout2.getComponent(0).value == "test component url2"
        assert urlFieldLayout2.getComponent(1) instanceof Button
        assert urlFieldLayout2.getComponent(1).icon.resourceId == "minus_sign.png"

        final Component addButton = target.getComponent(j++)
        assert addButton instanceof Button
        assert addButton.caption == "add component"

        final Component button = target.getComponent(j++)
        assert button instanceof Button
        assert button.caption == "done"
    }
}
