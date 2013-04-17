package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Table
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
import kikuyu.TestFixtures
import kikuyu.domain.UrlMapping

@TestMixin(GrailsUnitTestMixin)
class SpinnerFieldTest {

    private GrailsMock presenterMock

    private GrailsMock containerMock

    private GroovyObject propertyMockInstance

    private GrailsMock componentMock

    private MockFor propertyMock

    public void testSpinnerField() throws Exception {
        SpinnerField field = new SpinnerField()
        final HorizontalLayout component = field.getContentComponent()

        assert component.getComponent(0) instanceof Label
        assert component.getComponent(1) instanceof SpinnerButton
    }

    public void testUpClick() throws Exception {

        final UrlMapping urlMapping = TestFixtures.urlMappings[1]

        def field = createMocks urlMapping, { UrlMapping mapping1, UrlMapping mapping2, Table component ->
            assert mapping1 == urlMapping
            assert mapping2 == TestFixtures.urlMappings[0]
        }

        field.processClick(new SpinnerButton.ClickEvent(field, true))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    public void testUpClickFromTop() throws Exception {

        final UrlMapping urlMapping = TestFixtures.urlMappings[0]
        def field = createMocks(urlMapping, null)
        field.processClick(new SpinnerButton.ClickEvent(field, true))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    public void testDownClick() throws Exception {

        final UrlMapping urlMapping = TestFixtures.urlMappings[1]

        def field = createMocks urlMapping, { UrlMapping mapping1, UrlMapping mapping2, Table component ->
            assert mapping2 == urlMapping
            assert mapping1 == TestFixtures.urlMappings[2]
        }

        field.processClick(new SpinnerButton.ClickEvent(field, false))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    public void testDownClickAtBottom() throws Exception {

        final UrlMapping urlMapping = TestFixtures.urlMappings[2]
        def field = createMocks(urlMapping, null)

        field.processClick(new SpinnerButton.ClickEvent(field, false))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    def createMocks(def urlMapping, def switchMethod) {

        propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        presenterMock = mockFor(KikuyuPresenter)
        componentMock = mockFor(Table)
        containerMock = mockFor(Container)
        containerMock.demand.getItemIds {
            TestFixtures.urlMappings
        }

        if (switchMethod != null) {
            propertyMock.demand.getRow { urlMapping }
            presenterMock.demand.switchMatchOrder switchMethod
            componentMock.demand.setContainerDataSource() {}
        }


        propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])

        return new SpinnerField(propertyMockInstance, presenterMock.createMock(), containerMock.createMock(), componentMock.createMock())
    }
}
