package kikuyu.view

import com.vaadin.data.Property
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.BeanItem
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Button
import com.vaadin.ui.FormLayout
import kikuyu.domain.Page

class EditPageView extends FormLayout implements View {
    private KikuyuPresenter presenter
    private Page page

    EditPageView(KikuyuPresenter kikuyuPresenter, Page page) {
        this.page = page
        this.presenter = kikuyuPresenter
        setMargin(true)
        setSpacing(true)

        createForm()
    }

    private void createForm() {
        FieldGroup binder = new FieldGroup(new BeanItem(page));

        addComponent(binder.buildAndBind("Name", "name"));
        addComponent(binder.buildAndBind("Template URL", "url"));
        addComponent(binder.buildAndBind("Component URL", "componentUrl"));

        binder.fields.each {
            it.width = 500
            it.nullRepresentation = ""
            it.immediate = true
            it.addValueChangeListener({
                binder.commit()
                presenter.savePage(page)
            } as Property.ValueChangeListener)
        }

        addComponent(new Button("done", { presenter.navigator.navigateTo("") } as Button.ClickListener))
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
