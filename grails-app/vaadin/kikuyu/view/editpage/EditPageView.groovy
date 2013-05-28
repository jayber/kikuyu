package kikuyu.view.editpage

import com.vaadin.data.util.MethodProperty
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo
import kikuyu.domain.Page
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import util.FieldUtils

class EditPageView extends VerticalLayout implements View {
    private KikuyuPresenter presenter
    public Page page
    def FormLayout layout

    //no arg constructor needed by MockFor
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
        layout = new FormLayout()
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
            createNewPageComponentDisplay(pageComponent, layout)
        }
    }

    private void createNameField(FormLayout layout) {
        TextField field = new TextField("Name", new MethodProperty(page, "name"))
        layout.addComponent(field);
        FieldUtils.setUpField(field, presenter, page)
    }

    //default for test
    void createPageComponentAndField(Layout layout) {
        final PageComponent pageComponent = new PageComponent()
        page.addPageComponent(pageComponent)
        createNewPageComponentDisplay(pageComponent, layout)
    }

    private void createNewPageComponentDisplay(PageComponent pageComponent, Layout layout) {
        def component = new SinglePageComponent(pageComponent, presenter, this)
        layout.addComponent(component);
    }


    public void makeSlots() {
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
                createPageComponentAndField(layout)
            }
        }
    }

    void removePageComponent(SinglePageComponent pageComponent) {
        layout.removeComponent(pageComponent)
        page.pageComponents.remove(pageComponent.pageComponent)
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
