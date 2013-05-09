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
import kikuyu.domain.SubstitutionVariable

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

        HorizontalLayout scanLayout = new HorizontalLayout()
        scanLayout.width = "100%"

        final FormLayout grid = new FormLayout()
        grid.width = "100%"
        grid.setMargin(false)

        Button scan = new Button("scan", {
            final int slots = presenter.acquireNumberOfSlots(field.value)
            final String[] varNames = presenter.acquireSubstitutionVarNames(field.value)
            pageComponent.slots = slots
            if (slots > 0) {
                templateBox.value = true
            }
            List<SubstitutionVariable> vars = []
            for (String name : varNames) {
                vars.add(new SubstitutionVariable(name: name))
            }
            pageComponent.substitutionVariables = vars

            makeSlots(theWholeForm)
            makeSubstVarFields(grid, vars)
            presenter.savePage(page)
        } as Button.ClickListener)
        scan.immediate = true
        scanLayout.addComponent(scan)
        scanLayout.setComponentAlignment(scan, Alignment.TOP_LEFT)
        scanLayout.addComponent(grid)
        scanLayout.setComponentAlignment(grid, Alignment.BOTTOM_RIGHT)

        makeSubstVarFields(grid, pageComponent.substitutionVariables)

        componentLayout.addComponent(scanLayout, 0, 1)

        theWholeForm.addComponent(componentLayout);
    }

    def makeSubstVarFields(Layout grid, List<SubstitutionVariable> vars) {
        grid.removeAllComponents()
        if (vars != null && vars.size() > 0) {
            final Label title = new Label("Variables")
            title.setStyleName(Runo.LABEL_SMALL)
            grid.addComponent(title)
            for (SubstitutionVariable var : vars) {
                final TextField field = new TextField(var.name, new MethodProperty(var, "value"))
                setUpFieldInner(field)
                grid.addComponent(field)
                grid.setComponentAlignment(field, Alignment.TOP_RIGHT)
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
