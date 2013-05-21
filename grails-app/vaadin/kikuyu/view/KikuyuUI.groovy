package kikuyu.view

import com.vaadin.annotations.Theme
import com.vaadin.grails.Grails
import com.vaadin.server.Page
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI

@Theme("kikuyu")
class KikuyuUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        content = new KikuyuContainer(Grails.get(KikuyuPresenter), this)

        String homeLabel = Grails.i18n("default.home.label")
        Page.getCurrent().setTitle(homeLabel)
    }
}
