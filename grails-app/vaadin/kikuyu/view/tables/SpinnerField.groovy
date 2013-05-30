package kikuyu.view.tables

import com.vaadin.data.Container
import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.ui.*
import kikuyu.view.KikuyuPresenter
import kikuyu.view.tables.SpinnerButton.ClickEvent

class SpinnerField extends CustomField {

    private Container container
    private Table component

    KikuyuPresenter presenter

    RowMethodProperty dataSource

    SpinnerField() {
    }

    SpinnerField(Property property, KikuyuPresenter presenter, Container container, Component component) {
        this.component = component
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
                final prevRow = container.itemIds.find {
                    it.matchOrder == value.intValue() - 1
                }
                presenter.switchMatchOrder(dataSource.row, prevRow, container)
            }
        } else {
            final Object nextRow = container.itemIds.find {
                it.matchOrder == value.intValue() + 1
            }
            if (nextRow != null) {
                presenter.switchMatchOrder(nextRow, dataSource.row, container)
            }
        }
    }

    @Override
    Class getType() {
        return Number
    }

    static class RowMethodProperty extends MethodProperty {
        def row

        RowMethodProperty(Object instance, String beanPropertyName) {
            super(instance, beanPropertyName)
            this.row = instance
        }
    }
}
