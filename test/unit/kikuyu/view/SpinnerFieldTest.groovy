package kikuyu.view

import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
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
        def presenterMock = mockFor(KikuyuPresenter)
        def propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        final UrlMapping urlMapping = new UrlMapping(matchOrder: 1)
        propertyMock.demand.getRow { urlMapping }
        presenterMock.demand.incrementMatchOrder { UrlMapping mapping ->
            assert mapping == urlMapping
        }
        final GroovyObject propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])
        SpinnerField field = new SpinnerField(propertyMockInstance, presenterMock.createMock() as KikuyuPresenter)

        field.processClick(new SpinnerButton.ClickEvent(field, true))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    @Test
    public void testUpClickFromTop() throws Exception {
        def presenterMock = mockFor(KikuyuPresenter)
        def propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        final UrlMapping urlMapping = new UrlMapping(matchOrder: 0)

        final GroovyObject propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])
        SpinnerField field = new SpinnerField(propertyMockInstance, presenterMock.createMock() as KikuyuPresenter)

        field.processClick(new SpinnerButton.ClickEvent(field, true))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }

    @Test
    public void testDownClick() throws Exception {

        def presenterMock = mockFor(KikuyuPresenter)
        def propertyMock = new MockFor(SpinnerField.RowMethodProperty)
        final UrlMapping urlMapping = new UrlMapping(matchOrder: 1)
        propertyMock.demand.getRow { urlMapping }
        presenterMock.demand.decrementMatchOrder { UrlMapping mapping ->
            assert mapping == urlMapping
        }
        final GroovyObject propertyMockInstance = propertyMock.proxyDelegateInstance(
                [urlMapping, "matchOrder"] as Object[])
        SpinnerField field = new SpinnerField(propertyMockInstance, presenterMock.createMock() as KikuyuPresenter)

        field.processClick(new SpinnerButton.ClickEvent(field, false))

        presenterMock.verify()
        propertyMock.verify(propertyMockInstance)
    }
}
