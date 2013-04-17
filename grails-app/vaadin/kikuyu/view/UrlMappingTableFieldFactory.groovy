package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.grails.Grails
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Component
import com.vaadin.ui.DefaultFieldFactory
import com.vaadin.ui.Field

/**
 * This class renders a <code>com.vaadin.ui.ComboBox</code> field for properties called "page" (<code>kikuyu.domain.Page</code>)
 * properties and a <code>kikuyu.view.SpinnerField</code> for properties called "matchOrder" and only generates
 * fields for the currently selected row in tables, i.e. only the current row is editable in editable mode.
 */
class UrlMappingTableFieldFactory extends DefaultFieldFactory {

    KikuyuPresenter presenter
    Object currentSelectedItemId

    @Override
    Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
        if (currentSelectedItemId == itemId) {
            Field result = getPageComponent(propertyId, itemId)
            result = result ?: getMatchOrderComponent(propertyId, itemId, container, uiContext)
            return result ?: super.createField(container, itemId, propertyId, uiContext)
        }
        return null
    }

    private Field getMatchOrderComponent(propertyId, itemId, container, uiContext) {
        if (propertyId == "matchOrder") {
            final SpinnerField field = new SpinnerField(new SpinnerField.RowMethodProperty(itemId, "matchOrder"), presenter, container, uiContext)
            return field
        }
    }

    private Field getPageComponent(propertyId, itemId) {
        if (propertyId == "page") {
            final ComboBox box = new ComboBox(Grails.i18n("ui.urlmappingtable.page.select.caption"), presenter.listPageOptions())
            box.setPropertyDataSource(new MethodProperty(itemId, "page"))
            box.setWidth("100%")
            box.immediate = true
            box.addValueChangeListener({ Property.ValueChangeEvent evt ->
                presenter.saveRow(itemId)
            } as Property.ValueChangeListener)
            return box
        }
    }
}