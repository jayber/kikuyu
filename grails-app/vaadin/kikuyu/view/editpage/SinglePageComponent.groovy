package kikuyu.view.editpage

import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.server.ThemeResource
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import kikuyu.view.util.FieldUtils
import kikuyu.view.util.MapProperty
import org.apache.commons.lang.StringUtils

class SinglePageComponent extends VerticalLayout {

    private static final String VALID_URL_IMAGE_MSG = "Valid url"
    private static final String VALID_PATHSYMBOL_IMAGE_MSG = "Valid url symbol"
    private static final String INVALID_PATHSYMBOL_IMAGE_MSG = "Neither valid URL nor valid symbol"

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
        def varLayout = createScanAndAddVariablesButtons(presenter, layout)
        varLayout.addComponent(substitutionVarsLayout)
        makeSubstitutionVariableFields(pageComponent.substitutionVariables)
        componentLayout.addComponent(layout, 0, 1)
    }

    private GridLayout createLayouts() {
        substitutionVarsLayout = new VerticalLayout()

        final GridLayout componentLayout = new GridLayout(5, 2)
        this.addComponent(componentLayout)
        componentLayout.spacing = true
        return componentLayout
    }

    private Layout createScanAndAddVariablesButtons(KikuyuPresenter presenter, Layout layout) {
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
        componentLayout.addComponent(templateBox, 4, 0)
        componentLayout.setComponentAlignment(templateBox, Alignment.BOTTOM_RIGHT)
    }

    private void createAcceptPostCheckBox(PageComponent pageComponent, EditPageView container, KikuyuPresenter presenter, GridLayout componentLayout) {
        CheckBox box = new CheckBox("Accept POSTs?", new MethodProperty(pageComponent, "acceptPost"))
        box.immediate = true
        box.addValueChangeListener({
            box.commit()
            presenter.savePage(container.page)
        } as Property.ValueChangeListener)
        componentLayout.addComponent(box, 3, 0)
        componentLayout.setComponentAlignment(box, Alignment.BOTTOM_RIGHT)
    }

    private void createUrlField(PageComponent pageComponent, GridLayout componentLayout) {
        urlField = new TextField("Component URL", new MethodProperty(pageComponent, "url"));
        FieldUtils.setUpField(urlField, presenter, container.page)

        Image validImage = makeValidationImage("green_tick.png", VALID_URL_IMAGE_MSG)
        Image propertiesValidImage = makeValidationImage("properties_tick.png", VALID_PATHSYMBOL_IMAGE_MSG)
        Image invalidImage = makeValidationImage("red_cross.png", INVALID_PATHSYMBOL_IMAGE_MSG)

        def layout = new HorizontalLayout(validImage, propertiesValidImage, invalidImage)
        layout.spacing = true

        urlField.addValueChangeListener({ validateUrl(it, validImage, propertiesValidImage, invalidImage) } as Property.ValueChangeListener)
        componentLayout.addComponent(urlField, 0, 0)
        componentLayout.addComponent(layout, 1, 0)
    }

    private Image makeValidationImage(String imageName, String msg) {
        Image validImage = new Image("", new ThemeResource(imageName))
        validImage.visible = false
        validImage.alternateText = msg
        validImage.description = msg
        validImage.width = "24px"
        return validImage
    }

    private void createRemoveButton(KikuyuPresenter presenter, GridLayout componentLayout) {
        final Button removeButton = new Button("", { presenter.removeAction(this) } as Button.ClickListener)
        removeButton.setStyleName(Runo.BUTTON_LINK)
        removeButton.setIcon(new ThemeResource("minus_sign.png"))
        removeButton.description = "remove component"

        componentLayout.addComponent(removeButton, 2, 0)
        componentLayout.setComponentAlignment(removeButton, Alignment.BOTTOM_RIGHT)
    }

    def validateUrl(Property.ValueChangeEvent e, Image validUrlImage, Image propertiesValidImage, Image invalidImage) {
        String value = e.property.value
        validUrlImage.visible = false
        propertiesValidImage.visible = false
        invalidImage.visible = false
        if (StringUtils.isNotEmpty(value)) {
            if (presenter.urlSymbolResolver.isValidConcreteUrl(value)) {
                validUrlImage.visible = true
            } else if (presenter.urlSymbolResolver.isValidSymbolicUrl(value)) {
                propertiesValidImage.visible = true
            } else {
                invalidImage.visible = true
            }
        }
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
