package kikuyu.view

import com.vaadin.grails.Grails
import com.vaadin.ui.Component
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.Runo

class KikuyuComponent extends VerticalLayout {

    KikuyuPresenter presenter
    UI ui

    KikuyuComponent(KikuyuPresenter presenter, UI ui) {
        this.ui = ui
        this.presenter = presenter
        setSpacing(true)
        buildComponents()
    }

    private buildComponents() {
        final header = buildHeader()
        addComponent header
        setExpandRatio(header, 0)
        final body = buildBody()
        addComponent body
        setExpandRatio(body, 1)
    }

    private buildHeader() {
        final layout = new VerticalLayout()
        layout.setStyleName("kikuyu-header")
        layout.setMargin(true)

        layout.addComponent(buildPageTitle())
        layout.addComponent(buildDescription())
        return layout
    }

    private Component buildPageTitle() {
        String homeLabel = Grails.i18n("default.home.label")
        Label label = new Label(homeLabel)
        label.styleName = Runo.LABEL_H1
        label.addStyleName("kikuyu-header-label")
        return label
    }

    private Component buildDescription() {
        String homeLabel = Grails.i18n("default.description.label")
        Label label = new Label(homeLabel)
        return label
    }

    private Component buildBody() {
        VerticalLayout container = new VerticalLayout()
        presenter.buildNavigator(container, ui)
        return container
    }
}
