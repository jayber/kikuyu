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
import util.MapProperty

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

        final GridLayout componentLayout = new GridLayout(4, 2)
        componentLayout.spacing = true

        TextField field = new TextField("Component URL", new MethodProperty(pageComponent, "url"));
        setUpField(field)
        componentLayout.addComponent(field, 0, 0)
        final Button removeButton = new Button("", {
            theWholeForm.removeComponent(componentLayout)
            page.pageComponents.remove(pageComponent)
            presenter.savePage(page)
        } as Button.ClickListener)
        removeButton.setStyleName(Runo.BUTTON_LINK)
        removeButton.setIcon(new ThemeResource("minus_sign.png"))
        removeButton.description = "remove component"

        componentLayout.addComponent(removeButton, 1, 0)
        componentLayout.setComponentAlignment(removeButton, Alignment.BOTTOM_RIGHT)

        CheckBox box = new CheckBox("Accept POSTs?", new MethodProperty(pageComponent, "acceptPost"))
        box.immediate = true
        box.addValueChangeListener({
            box.commit()
            presenter.savePage(page)
        } as Property.ValueChangeListener)
        componentLayout.addComponent(box, 2, 0)
        componentLayout.setComponentAlignment(box, Alignment.BOTTOM_RIGHT)

        CheckBox templateBox = new CheckBox("Template?", new MethodProperty(pageComponent, "template"))
        templateBox.immediate = true
        templateBox.addValueChangeListener({
            templateBox.commit()
            presenter.savePage(page)
        } as Property.ValueChangeListener)
        componentLayout.addComponent(templateBox, 3, 0)
        componentLayout.setComponentAlignment(templateBox, Alignment.BOTTOM_RIGHT)

        final FormLayout subFormLayout = new FormLayout()

        Button scanButton = new Button("scan", {
            final int slots = presenter.acquireNumberOfSlots(field.value)
            final String[] varNames = presenter.acquireSubstitutionVarNames(field.value)
            pageComponent.slots = slots
            if (slots > 0) {
                templateBox.value = true
            }

            def vars = [:]
            for (String name : varNames) {
                vars.put(name, "")
            }
            pageComponent.substitutionVariables = vars

            makeSlots(theWholeForm)
            makeSubstVarFields(subFormLayout, pageComponent.substitutionVariables)
            presenter.savePage(page)
        } as Button.ClickListener)
        scanButton.immediate = true
        scanButton.setSizeUndefined()

        final HorizontalLayout innerLayout = new HorizontalLayout(scanButton, subFormLayout)
        componentLayout.addComponent(innerLayout, 0, 1)
        innerLayout.setComponentAlignment(subFormLayout, Alignment.TOP_RIGHT)

        makeSubstVarFields(subFormLayout, pageComponent.substitutionVariables)

        theWholeForm.addComponent(componentLayout);
    }

    def makeSubstVarFields(Layout layout, Map data) {
        layout.removeAllComponents()
        if (data != null && data.size() > 0) {
            final Label title = new Label("Variables")
            title.setStyleName(Runo.LABEL_SMALL)
            layout.addComponent(title)

            data.each {
                final TextField field = new TextField(it.key, new MapProperty(bean: data, propertyName: it.key, propertyClass: String.class))
                field.width = 250
                setUpFieldInner(field)
                layout.addComponent(field)
                layout.setComponentAlignment(field, Alignment.TOP_LEFT)
            }
        }
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
        setUpFieldInner(field)
    }

    private void setUpFieldInner(Field field) {
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
