package kikuyu.view

import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.TextField
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.Page
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
        page.url = "test template url"
        page.componentUrl = "test component url"
        mockForPresenter = mockFor(KikuyuPresenter)
        presenter = mockForPresenter.createMock()
        target = new EditPageView(presenter, page)
    }

    void testEnter() {
        int i = 0
        final Component nameField = target.getComponent(i++)
        assert nameField instanceof TextField
        assert nameField.caption == "Name"
        assert nameField.value == "test name"

        final Component urlField = target.getComponent(i++)
        assert urlField instanceof TextField
        assert urlField.caption == "Template URL"
        assert urlField.value == "test template url"

        final Component componentUrlField = target.getComponent(i++)
        assert componentUrlField instanceof TextField
        assert componentUrlField.caption == "Component URL"
        assert componentUrlField.value == "test component url"

        final Component button = target.getComponent(i++)
        assert button instanceof Button
        assert button.caption == "done"
    }
}
