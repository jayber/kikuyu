package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.ui.*
import kikuyu.view.SpinnerButton.ClickEvent

class SpinnerField extends CustomField {

    private Container container
    static class RowMethodProperty extends MethodProperty {
        def row

        RowMethodProperty(Object instance, String beanPropertyName) {
            super(instance, beanPropertyName)
            this.row = instance
        }
    }


    KikuyuPresenter presenter

    RowMethodProperty dataSource

    SpinnerField() {
    }

    SpinnerField(Property property, KikuyuPresenter presenter, Container container) {
        this.container = container
        this.dataSource = property
        setPropertyDataSource(property)
        this.presenter = presenter
    }

    public Component getContentComponent() {
        return getContent()
    }

    @Override
    protected Component initContent() {
        final HorizontalLayout layout = new HorizontalLayout()
        layout.spacing = true

        final Label label = new Label(getPropertyDataSource())
        layout.addComponent(label)
        layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT)

        final SpinnerButton button = new SpinnerButton()
        button.addSpinnerClickListener({ event -> processClick(event) } as SpinnerButton.SpinnerClickListener)
        layout.addComponent(button)
        layout.setComponentAlignment(button, Alignment.MIDDLE_LEFT)

        return layout
    }

    void processClick(ClickEvent event) {
        final BigInteger value = (BigInteger) getValue()
        if (event.up) {
            if (value.compareTo(0)) {
                presenter.switchMatchOrder(dataSource.row, container.itemIds.find {
                    it.matchOrder == value.intValue() - 1
                })
            }
        } else {
            presenter.switchMatchOrder(container.itemIds.find {
                it.matchOrder == value.intValue() + 1
            }, dataSource.row)
        }
    }

    @Override
    Class getType() {
        return Number
    }
}
