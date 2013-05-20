package kikuyu.view.editpage

import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.TextField
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
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

        mockForPresenter = mockFor(KikuyuPresenter)
        mockForPresenter.demand.getNavigateHomeAction(0) {
            def nothing = {
                "do nothing"
            }
        }
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

        def layout1 = layout.getComponent(i++)
        assert layout1 instanceof SinglePageComponent

        final Component addButton = target.getComponent(j++)
        assert addButton instanceof Button
        assert addButton.caption == "add component"

        final Component button = target.getComponent(j++)
        assert button instanceof Button
        assert button.caption == "done"
    }
}
