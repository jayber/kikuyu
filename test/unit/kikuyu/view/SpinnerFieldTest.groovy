package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
import kikuyu.TestFixtures
import kikuyu.domain.UrlMapping
import org.junit.Test

@TestMixin(GrailsUnitTestMixin)
class SpinnerFieldTest {

    public void testSpinnerField() throws Exception {
        SpinnerField field = new SpinnerField()
        final HorizontalLayout component = field.getContentComponent()

        assert component.getComponent(0) instanceof Label
        assert component.getComponent(1) instanceof SpinnerButton
    }

    @Test
    public void testUpClick() throws Exception {
        def propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        final UrlMapping urlMapping = TestFixtures.urlMappings[1]
        propertyMock.demand.getRow { urlMapping }
        def presenterMock = mockFor(KikuyuPresenter)
        presenterMock.demand.switchMatchOrder { UrlMapping mapping1, UrlMapping mapping2 ->
            assert mapping1 == urlMapping
            assert mapping2 == TestFixtures.urlMappings[0]
        }
        def containerMock = mockFor(Container)
        containerMock.demand.getItemIds {
            TestFixtures.urlMappings
        }

        final GroovyObject propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])
        SpinnerField field = new SpinnerField(propertyMockInstance, presenterMock.createMock(), containerMock.createMock())

        field.processClick(new SpinnerButton.ClickEvent(field, true))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    @Test
    public void testUpClickFromTop() throws Exception {

        def propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        final UrlMapping urlMapping = TestFixtures.urlMappings[0]
        def presenterMock = mockFor(KikuyuPresenter)
        def containerMock = mockFor(Container)
        containerMock.demand.getItemIds {
            TestFixtures.urlMappings
        }

        final GroovyObject propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])
        SpinnerField field = new SpinnerField(propertyMockInstance, presenterMock.createMock(), containerMock.createMock())

        field.processClick(new SpinnerButton.ClickEvent(field, true))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    @Test
    public void testDownClick() throws Exception {

        def propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        final UrlMapping urlMapping = TestFixtures.urlMappings[1]
        propertyMock.demand.getRow { urlMapping }
        def presenterMock = mockFor(KikuyuPresenter)
        presenterMock.demand.switchMatchOrder { UrlMapping mapping1, UrlMapping mapping2 ->
            assert mapping2 == urlMapping
            assert mapping1 == TestFixtures.urlMappings[2]
        }
        def containerMock = mockFor(Container)
        containerMock.demand.getItemIds {
            TestFixtures.urlMappings
        }

        final GroovyObject propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])
        SpinnerField field = new SpinnerField(propertyMockInstance, presenterMock.createMock(), containerMock.createMock())

        field.processClick(new SpinnerButton.ClickEvent(field, false))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    @Test
    public void testDownClickAtBottom() throws Exception {

        def propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        final UrlMapping urlMapping = TestFixtures.urlMappings[2]

        def presenterMock = mockFor(KikuyuPresenter)
        def containerMock = mockFor(Container)
        containerMock.demand.getItemIds {
            TestFixtures.urlMappings
        }

        final GroovyObject propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])
        SpinnerField field = new SpinnerField(propertyMockInstance, presenterMock.createMock(), containerMock.createMock())

        field.processClick(new SpinnerButton.ClickEvent(field, false))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }
}
