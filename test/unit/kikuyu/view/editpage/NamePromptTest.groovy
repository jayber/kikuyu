package kikuyu.view.editpage

import com.vaadin.data.Validator
import com.vaadin.data.Validator.EmptyValueException
import com.vaadin.ui.TextField
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class NamePromptTest {

    public void testInit() throws Exception {
        def prompt = new NamePrompt({})

        def layout = prompt.getComponent(0)
        def field = layout.getComponent(0)
        def button = layout.getComponent(1)

        assert "name" == field.inputPrompt
        assert "ok" == button.caption

        def validationLabel = prompt.getComponent(1)
        assert "Enter a valid name" == validationLabel.value
    }

    public void testValidationEmpty() throws Exception {
        def prompt = new NamePrompt({})

        def layout = prompt.getComponent(0)
        TextField field = layout.getComponent(0)

        field.value = ""
        try {
            field.validate()
        } catch (EmptyValueException e) {
        }

        assertFalse field.isValid()
    }

    public void testValidationInvalidChar() throws Exception {
        def prompt = new NamePrompt({})

        def layout = prompt.getComponent(0)
        TextField field = layout.getComponent(0)

        field.value = "test<"
        try {
            field.validate()
        } catch (Validator.InvalidValueException e) {
        }

        assertFalse field.isValid()
    }

    public void testValidationSuccess() throws Exception {
        def prompt = new NamePrompt({})

        def layout = prompt.getComponent(0)
        TextField field = layout.getComponent(0)

        field.value = "test"
        field.validate()

        assert field.isValid()
    }

    public void testOkActionFail() throws Exception {
        def prompt = new NamePrompt({
            fail "should not succeed"
        })

        prompt.okAction()
        assert prompt.validationMessage.visible
    }

    public void testOkActionSucceed() throws Exception {
        def prompt = new NamePrompt({ String value ->
            assert "test" == value
        })

        prompt.field.value = "test"
        prompt.okAction()
        assertFalse prompt.validationMessage.visible
    }
}
