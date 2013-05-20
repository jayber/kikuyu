package kikuyu.view.editpage

import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter

class EditPageView extends VerticalLayout implements View {
    private KikuyuPresenter presenter
    private Page page

    EditPageView() {
    }

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

        createNameField(layout)

        createPageComponents(layout)

        createAddComponentButton(layout)

        addComponent(new Button("done", presenter.navigateHomeAction as Button.ClickListener))
    }

    private void createAddComponentButton(FormLayout layout) {
        final Button addComponentButton = new Button("add component", {
            createPageComponentAndField(layout)
        } as Button.ClickListener)
        addComponentButton.setStyleName(Runo.BUTTON_SMALL)
        addComponent(addComponentButton)
    }

    private void createPageComponents(FormLayout layout) {
        for (PageComponent pageComponent : page.pageComponents) {
            createNewPageComponentField(pageComponent, layout)
        }
    }

    private void createNameField(FormLayout layout) {
        TextField field = new TextField("Name", new MethodProperty(page, "name"))
        layout.addComponent(field);
        setUpField(field)
    }

    private void createPageComponentAndField(layout) {
        final PageComponent pageComponent = new PageComponent()
        page.addPageComponent(pageComponent)
        createNewPageComponentField(pageComponent, layout)
    }

    private void createNewPageComponentField(PageComponent pageComponent, Layout layout) {
        def component = new SinglePageComponent(pageComponent, layout, presenter, page, this)
        layout.addComponent(component);
    }


    def makeSlots(theWholeForm) {
        final List<PageComponent> components = page.pageComponents
        int noComponents = components.size()

        int requiredSlots = 1
        for (PageComponent comp : components) {
            final int slots = comp.slots == null ? 0 : comp.slots
            requiredSlots = requiredSlots + slots
        }

        final int diff = requiredSlots - noComponents
        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                createPageComponentAndField(theWholeForm)
            }
        }
    }

    def setUpField(Field field) {
        field.width = 500
        setUpFieldInner(field)
    }

    def setUpFieldInner(Field field) {
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
