package kikuyu.view.editpage

import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.server.ThemeResource
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import util.FieldUtils
import util.MapProperty

class SinglePageComponent extends VerticalLayout {

    def EditPageView container
    def PageComponent pageComponent
    private KikuyuPresenter presenter

    private VerticalLayout substitutionVarsLayout
    def CheckBox templateBox
    private TextField urlField

    //no arg constructor needed by MockFor
    SinglePageComponent() {
    }

    SinglePageComponent(PageComponent pageComponent, KikuyuPresenter presenter, EditPageView container) {

        this.presenter = presenter
        this.pageComponent = pageComponent
        this.container = container

        GridLayout componentLayout = createLayouts()

        createUrlField(pageComponent, componentLayout)

        createRemoveButton(presenter, componentLayout)

        createAcceptPostCheckBox(pageComponent, container, presenter, componentLayout)

        createTemplateCheckbox(pageComponent, container, presenter, componentLayout)

        createScanAndVariables(presenter, pageComponent, componentLayout)
    }

    private void createScanAndVariables(KikuyuPresenter presenter, PageComponent pageComponent, GridLayout componentLayout) {
        HorizontalLayout layout = new HorizontalLayout()
        layout.setWidth("100%")
        def varLayout = createScanAndSubstVariables(presenter, layout)
        varLayout.addComponent(substitutionVarsLayout)
        makeSubstitutionVariableFields(pageComponent.substitutionVariables)
        componentLayout.addComponent(layout, 0, 1)
    }

    private GridLayout createLayouts() {
        substitutionVarsLayout = new VerticalLayout()

        final GridLayout componentLayout = new GridLayout(4, 2)
        this.addComponent(componentLayout)
        componentLayout.spacing = true
        return componentLayout
    }

    private Layout createScanAndSubstVariables(KikuyuPresenter presenter, Layout layout) {
        Button scanButton = new Button("scan", { presenter.scanAction(this) } as Button.ClickListener)
        scanButton.immediate = true
        scanButton.setSizeUndefined()
        layout.addComponent(scanButton)
        final VerticalLayout varsLayout = new VerticalLayout()
        varsLayout.setWidth("100%")
        layout.addComponent(varsLayout)
        final Button addVarButton = new Button("add variable", { presenter.addSubstitutionVar(this) } as Button.ClickListener)
        addVarButton.setStyleName(Runo.BUTTON_SMALL)
        addVarButton.immediate = true
        varsLayout.addComponent(addVarButton)
        varsLayout.setComponentAlignment(addVarButton, Alignment.TOP_RIGHT)
        varsLayout
    }

    private void createTemplateCheckbox(PageComponent pageComponent, EditPageView container, KikuyuPresenter presenter, GridLayout componentLayout) {
        templateBox = new CheckBox("Template?", new MethodProperty(pageComponent, "template"))
        templateBox.immediate = true
        templateBox.addValueChangeListener({
            templateBox.commit()
            presenter.savePage(container.page)
        } as Property.ValueChangeListener)
        componentLayout.addComponent(templateBox, 3, 0)
        componentLayout.setComponentAlignment(templateBox, Alignment.BOTTOM_RIGHT)
    }

    private void createAcceptPostCheckBox(PageComponent pageComponent, EditPageView container, KikuyuPresenter presenter, GridLayout componentLayout) {
        CheckBox box = new CheckBox("Accept POSTs?", new MethodProperty(pageComponent, "acceptPost"))
        box.immediate = true
        box.addValueChangeListener({
            box.commit()
            presenter.savePage(container.page)
        } as Property.ValueChangeListener)
        componentLayout.addComponent(box, 2, 0)
        componentLayout.setComponentAlignment(box, Alignment.BOTTOM_RIGHT)
    }

    private void createRemoveButton(KikuyuPresenter presenter, GridLayout componentLayout) {
        final Button removeButton = new Button("", { presenter.removeAction(this) } as Button.ClickListener)
        removeButton.setStyleName(Runo.BUTTON_LINK)
        removeButton.setIcon(new ThemeResource("minus_sign.png"))
        removeButton.description = "remove component"

        componentLayout.addComponent(removeButton, 1, 0)
        componentLayout.setComponentAlignment(removeButton, Alignment.BOTTOM_RIGHT)
    }

    private void createUrlField(PageComponent pageComponent, GridLayout componentLayout) {
        urlField = new TextField("Component URL", new MethodProperty(pageComponent, "url"));
        FieldUtils.setUpField(urlField, presenter, container.page)
        componentLayout.addComponent(urlField, 0, 0)
    }

    private void makeSubstitutionVariableFields(Map data) {
        substitutionVarsLayout.removeAllComponents()
        if (data != null && data.size() > 0) {
            final Label title = new Label("Variables")
            title.setStyleName(Runo.LABEL_SMALL)
            substitutionVarsLayout.addComponent(title)

            data.each {
                final String varName = it.key
                final TextField field = new TextField(varName, new MapProperty(bean: data, propertyName: varName, propertyClass: String.class))
                field.width = 250
                FieldUtils.setUpFieldInner(field, presenter, container.page)

                final Button removeButton = new Button("", { removeVariable(varName) } as Button.ClickListener)
                removeButton.setStyleName(Runo.BUTTON_LINK)
                removeButton.setIcon(new ThemeResource("minus_sign_grey.png"))
                removeButton.description = "remove variable"

                final HorizontalLayout layout = new HorizontalLayout(field, removeButton)
                layout.setComponentAlignment(removeButton, Alignment.BOTTOM_RIGHT)
                layout.spacing = true

                substitutionVarsLayout.addComponent(layout)
                substitutionVarsLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT)
            }
        }
    }

    def removeVariable(String varName) {
        pageComponent.substitutionVariables.remove(varName)
        makeSubstitutionVariableFields(pageComponent.substitutionVariables)
    }

    String getUrl() {
        urlField.value
    }

    void setSlots(int slots) {
        pageComponent.slots = slots
        if (slots > 0) {
            templateBox.value = true
        }
    }

    void setSubstitutionVariables(Map vars) {
        pageComponent.substitutionVariables = vars
        makeSubstitutionVariableFields(pageComponent.substitutionVariables)
    }

    void addSubstitutionVariable(String varName) {
        pageComponent.substitutionVariables.put(varName, "")
        makeSubstitutionVariableFields(pageComponent.substitutionVariables)
    }
}
