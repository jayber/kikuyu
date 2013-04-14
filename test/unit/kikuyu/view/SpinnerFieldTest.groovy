package kikuyu.view

import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
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
    public void testEventUp() throws Exception {
        def mock = mockFor(KikuyuPresenter)
        mock.demand.incrementMatchOrder { UrlMapping mapping -> }
        SpinnerField field = new SpinnerField(mock.createMock() as KikuyuPresenter)
        field.setValue(1)

        field.processClick(new SpinnerButton.ClickEvent(field, true))

        mock.verify()
    }

    @Test
    public void testEventUpFromTop() throws Exception {
        def mock = mockFor(KikuyuPresenter)
        SpinnerField field = new SpinnerField(mock.createMock())
        field.setValue(0)

        field.processClick(new SpinnerButton.ClickEvent(field, true))

        mock.verify()
    }

    @Test
    public void testEventDown() throws Exception {
        def mock = mockFor(KikuyuPresenter)
        mock.demand.decrementMatchOrder { UrlMapping mapping -> }
        SpinnerField field = new SpinnerField(mock.createMock())

        field.processClick(new SpinnerButton.ClickEvent(field, false))

        mock.verify()
    }
}
