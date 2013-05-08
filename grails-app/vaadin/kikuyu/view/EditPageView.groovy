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
            createPageComponentAndField(layout)
        } as Button.ClickListener)
        addComponentButton.setStyleName(Runo.BUTTON_SMALL)
        addComponent(addComponentButton)

        addComponent(new Button("done", { presenter.navigator.navigateTo("") } as Button.ClickListener))
    }

    private void createPageComponentAndField(layout) {
        final PageComponent pageComponent = new PageComponent()
        page.addPageComponent(pageComponent)
        createNewPageComponentField(pageComponent, layout)
    }

    private void createNewPageComponentField(PageComponent pageComponent, FormLayout theWholeForm) {

        final VerticalLayout componentLayout = new VerticalLayout()
        componentLayout.spacing = true

        final HorizontalLayout fieldLayout = new HorizontalLayout()
        componentLayout.addComponent(fieldLayout)
        fieldLayout.spacing = true

        TextField field = new TextField("Component URL", new MethodProperty(pageComponent, "url"));
        setUpField(field)
        fieldLayout.addComponent(field)
        final Button removeButton = new Button("", {
            theWholeForm.removeComponent(componentLayout)
            page.pageComponents.remove(pageComponent)
            presenter.savePage(page)
        } as Button.ClickListener)
        removeButton.setStyleName(Runo.BUTTON_LINK)
        removeButton.setIcon(new ThemeResource("minus_sign.png"))
        removeButton.description = "remove component"

        fieldLayout.addComponent(removeButton)
        fieldLayout.setComponentAlignment(removeButton, Alignment.BOTTOM_RIGHT)

        CheckBox box = new CheckBox("Accept POSTs?", new MethodProperty(pageComponent, "acceptPost"))
        box.immediate = true
        box.addValueChangeListener({
            box.commit()
            presenter.savePage(page)
        } as Property.ValueChangeListener)
        fieldLayout.addComponent(box)
        fieldLayout.setComponentAlignment(box, Alignment.BOTTOM_RIGHT)


        VerticalLayout scanLayout
        CheckBox templateBox = new CheckBox("Template?", new MethodProperty(pageComponent, "template"))
        templateBox.immediate = true
        templateBox.addValueChangeListener({
            templateBox.commit()
            presenter.savePage(page)
            scanLayout.visible = pageComponent.template ?: false
        } as Property.ValueChangeListener)
        fieldLayout.addComponent(templateBox)
        fieldLayout.setComponentAlignment(templateBox, Alignment.BOTTOM_RIGHT)

        scanLayout = new VerticalLayout()
        scanLayout.visible = pageComponent.template ?: false

        Button scan = new Button("scan", {
            final int slots = presenter.acquireNumberOfSlots(field.value)
            final String[] varNames = presenter.acquireSubstitutionVarNames(field.value)
            pageComponent.slots = slots
            presenter.savePage(page)
            makeSlots(theWholeForm)
        } as Button.ClickListener)
        scan.immediate = true
        scanLayout.addComponent(scan)

        componentLayout.addComponent(scanLayout)

        theWholeForm.addComponent(componentLayout);
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
