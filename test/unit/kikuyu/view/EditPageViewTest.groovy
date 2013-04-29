package kikuyu.view

import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.TextField
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

        final Component urlField = layout.getComponent(i++)
        assert urlField instanceof TextField
        assert urlField.caption == "Component URL"
        assert urlField.value == "test component url1"

        final Component componentUrlField = layout.getComponent(i++)
        assert componentUrlField instanceof TextField
        assert componentUrlField.caption == "Component URL"
        assert componentUrlField.value == "test component url2"

        final Component addButton = target.getComponent(j++)
        assert addButton instanceof Button
        assert addButton.caption == "add component"

        final Component button = target.getComponent(j++)
        assert button instanceof Button
        assert button.caption == "done"
    }
}
