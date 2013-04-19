package kikuyu.view

import com.vaadin.server.ExternalResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.Runo

class SpinnerButton extends VerticalLayout {
    private static final String UP_ARROW = "images/spinner_up.png"
    private static final String DOWN_ARROW = "images/spinner_down.png"

    SpinnerButton() {
        super()
        initContent()
    }

    protected void initContent() {
        createButton(UP_ARROW)
        createButton(DOWN_ARROW)
    }

    private void createButton(final String imagePath) {
        final Button button = new Button()
        button.setIcon(new ExternalResource(imagePath))
        button.setStyleName(Runo.BUTTON_LINK)
        button.addClickListener({ event -> processClick(imagePath == UP_ARROW) } as Button.ClickListener)
        addComponent(button)
        setComponentAlignment(button, Alignment.TOP_LEFT)
    }

    void addSpinnerClickListener(SpinnerClickListener listener) {
        addListener(ClickEvent, listener, listener.getClass().getMethod("spinnerClicked", ClickEvent))
    }

    void processClick(boolean up) {
        fireEvent(new ClickEvent(this, up))
    }

    static class ClickEvent extends Button.ClickEvent {
        boolean up

        ClickEvent(Component source, boolean up) {
            super(source)
            this.up = up
        }
    }

    interface SpinnerClickListener {
        void spinnerClicked(ClickEvent event)
    }
}
