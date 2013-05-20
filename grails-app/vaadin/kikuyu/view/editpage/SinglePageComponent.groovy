package kikuyu.view.editpage

import com.vaadin.data.Property
import com.vaadin.data.util.MethodProperty
import com.vaadin.server.ThemeResource
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import util.MapProperty

class SinglePageComponent extends VerticalLayout {

    private EditPageView container
    private Page page
    private PageComponent pageComponent
    private KikuyuPresenter presenter
    private Layout theWholeForm

    SinglePageComponent(PageComponent pageComponent, Layout theWholeForm, KikuyuPresenter presenter, Page page, EditPageView container) {

        this.theWholeForm = theWholeForm
        this.presenter = presenter
        this.pageComponent = pageComponent
        this.page = page
        this.container = container

        final GridLayout componentLayout = new GridLayout(4, 2)
        this.addComponent(componentLayout)
        componentLayout.spacing = true

        TextField field = new TextField("Component URL", new MethodProperty(pageComponent, "url"));
        container.setUpField(field)
        componentLayout.addComponent(field, 0, 0)

        final Button removeButton = new Button("", removeAction as Button.ClickListener)
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

            container.makeSlots(theWholeForm)
            makeSubstVarFields(subFormLayout, pageComponent.substitutionVariables)
            presenter.savePage(page)
        } as Button.ClickListener)
        scanButton.immediate = true
        scanButton.setSizeUndefined()

        final HorizontalLayout innerLayout = new HorizontalLayout(scanButton, subFormLayout)
        componentLayout.addComponent(innerLayout, 0, 1)
        innerLayout.setComponentAlignment(subFormLayout, Alignment.TOP_RIGHT)

        makeSubstVarFields(subFormLayout, pageComponent.substitutionVariables)

    }

    def removeAction = {
        theWholeForm.removeComponent(this)
        page.pageComponents.remove(pageComponent)
        presenter.savePage(page)
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
                container.setUpFieldInner(field)
                layout.addComponent(field)
                layout.setComponentAlignment(field, Alignment.TOP_LEFT)
            }
        }
    }
}
