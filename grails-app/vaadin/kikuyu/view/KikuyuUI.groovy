package kikuyu.view

import com.vaadin.annotations.Theme
import com.vaadin.grails.Grails
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI

@Theme("runo")
class KikuyuUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        content = new KikuyuComponent(Grails.get(KikuyuPresenterImpl))
    }
}
