package kikuyu.view.editpage

import com.vaadin.ui.*
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import org.junit.Before

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

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

    void testCreatePageComponentAndField() {
        def mockLayout = mockFor(Layout)
        mockLayout.demand.addComponent(1) { SinglePageComponent component -> }
        def mockInstance = mockLayout.createMock()
        target.createPageComponentAndField(mockInstance)

        assert 2 == page.pageComponents.size()
        mockLayout.verify()
    }

    public void testMakeSlots() throws Exception {
        def pageComponents = []
        pageComponents.add(new PageComponent(url: "test component url1", slots: 2))
        pageComponents.add(new PageComponent(url: "test component url2", slots: 1))

        target.page.pageComponents = pageComponents
        target.makeSlots()

        assert 4 == target.page.pageComponents.size()
    }

    public void testRemoveComponent() throws Exception {
        def component = new MockFor(SinglePageComponent)
        component.demand.getPageComponent {
            page.pageComponents[0]
        }

        FormLayout layout = mock(FormLayout)
        target.layout = layout
        Object mockComponentInstance = component.proxyInstance()
        target.removePageComponent(mockComponentInstance)

        component.verify(mockComponentInstance)
        verify(layout).removeComponent(mockComponentInstance)
    }
}
