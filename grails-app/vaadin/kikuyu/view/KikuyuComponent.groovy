package kikuyu.view

import com.vaadin.grails.Grails
import com.vaadin.navigator.Navigator
import com.vaadin.ui.Component
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.Runo

class KikuyuComponent extends VerticalLayout {

    KikuyuPresenter presenter
    UI ui
    private Navigator navigator

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

        layout.addComponent(buildPageTitle())

        layout.setMargin(true)
        return layout
    }

    private Component buildPageTitle() {
        String homeLabel = Grails.i18n("default.home.label")
        Label label = new Label(homeLabel)
        label.styleName = Runo.LABEL_H1
        return label
    }

    private Component buildBody() {

        VerticalLayout container = new VerticalLayout()

        def navigator = new Navigator(ui, container)
        presenter.navigator = navigator
        navigator.addView("", new DataTablesView(presenter))

        return container
    }
}
