package kikuyu.view.editpage

import com.vaadin.data.Validator
import com.vaadin.ui.*

import java.util.regex.Pattern

class NamePrompt extends VerticalLayout {
    private static final Pattern pattern = ~/[a-zA-Z0-9\-_]+/
    private Closure onSuccess
    def TextField field
    def Label validationMessage

    NamePrompt(def onSuccess) {
        this.onSuccess = onSuccess
        field = new TextField()
        field.inputPrompt = "name"
        field.required = true
        field.validationVisible = true
        field.addValidator(new Validator() {
            @Override
            void validate(Object value) throws Validator.InvalidValueException {
                if (!pattern.matcher(value).matches()) {
                    throw new Validator.InvalidValueException("Enter a valid name")
                }
            }
        })

        validationMessage = new Label("Enter a valid name")
        validationMessage.setStyleName("validation-fail")
        validationMessage.visible = false

        final Button button = new Button("ok", okAction as Button.ClickListener)
        final HorizontalLayout layout = new HorizontalLayout(field, button)
        layout.spacing = true
        this.addComponents(layout, validationMessage)
        spacing = true
        setMargin(true)
    }

    def okAction = {
        boolean success = true
        try {
            field.validate()
        } catch (Validator.InvalidValueException e) {
            success = false
            validationMessage.visible = true
        }
        if (success) {
            onSuccess(field.value)
        }
    }
}
