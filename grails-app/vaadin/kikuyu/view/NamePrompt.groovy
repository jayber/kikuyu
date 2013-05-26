package kikuyu.view

import com.vaadin.data.Validator
import com.vaadin.ui.*

import java.util.regex.Pattern

class NamePrompt extends VerticalLayout {
    private static final Pattern pattern = ~/[a-zA-Z0-9\-_]+/

    NamePrompt(def onSuccess) {
        final TextField field = new TextField()
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

        final Label validationMessage = new Label("error msg")
        validationMessage.setStyleName("validation-fail")
        validationMessage.visible = false

        final Button button = new Button("ok", {
            boolean fail = false
            try {
                field.validate()
            } catch (Validator.InvalidValueException e) {
                fail = true
                validationMessage.value = "Enter a valid name"
                validationMessage.visible = true
            }
            if (!fail) {
                onSuccess(field.value)
            }
        } as Button.ClickListener)
        final HorizontalLayout layout = new HorizontalLayout(field, button)
        layout.spacing = true
        this.addComponents(layout, validationMessage)
        spacing = true
        setMargin(true)
    }
}
