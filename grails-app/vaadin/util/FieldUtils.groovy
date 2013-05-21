package util

import com.vaadin.data.Property
import com.vaadin.ui.Field
import kikuyu.domain.Page
import kikuyu.view.KikuyuPresenter

class FieldUtils {

    static void setUpField(Field field, KikuyuPresenter presenter, Page page) {
        field.width = 500
        setUpFieldInner(field, presenter, page)
    }

    static void setUpFieldInner(Field field, KikuyuPresenter presenter, Page page) {
        field.nullRepresentation = ""
        field.immediate = true
        field.addValueChangeListener({
            field.commit()
            presenter.savePage(page)
        } as Property.ValueChangeListener)
    }
}
