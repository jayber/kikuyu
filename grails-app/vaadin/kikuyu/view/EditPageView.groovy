package kikuyu.view

import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.ThemeResource
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo
import kikuyu.domain.Page
import kikuyu.domain.PageComponent

class EditPageView extends VerticalLayout implements View {
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
        final FormLayout layout = new FormLayout()
        addComponent(layout)

        TextField field = new TextField("Name", new MethodProperty(page, "name"))
        layout.addComponent(field);
        setUpField(field)

        for (PageComponent pageComponent : page.pageComponents) {
            createNewPageComponentField(pageComponent, layout)
        }

        final Button addComponentButton = new Button("add component", {
            final PageComponent pageComponent = new PageComponent()
            page.addPageComponent(pageComponent)
            createNewPageComponentField(pageComponent, layout)
        } as Button.ClickListener)
        addComponentButton.setStyleName(Runo.BUTTON_SMALL)
        addComponent(addComponentButton)

        addComponent(new Button("done", { presenter.navigator.navigateTo("") } as Button.ClickListener))
    }

    private void createNewPageComponentField(PageComponent pageComponent, FormLayout layout) {
        TextField field = new TextField("Component URL", new MethodProperty(pageComponent, "url"));

        final HorizontalLayout fieldLayout = new HorizontalLayout(field)
        fieldLayout.spacing = true
        final Button removeButton = new Button("", {
            layout.removeComponent(fieldLayout)
            page.pageComponents.remove(pageComponent)
        } as Button.ClickListener)
        removeButton.setStyleName(Runo.BUTTON_LINK)
        removeButton.setIcon(new ThemeResource("minus_sign.png"))
        removeButton.description = "remove component"

        fieldLayout.addComponent(removeButton)
        fieldLayout.setComponentAlignment(removeButton, Alignment.BOTTOM_RIGHT)

        layout.addComponent(fieldLayout);
        setUpField(field)
    }

    private void setUpField(Field field) {
        field.width = 500
        field.nullRepresentation = ""
        field.immediate = true
        field.addValueChangeListener({
            field.commit()
            presenter.savePage(page)
        } as Property.ValueChangeListener)
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
