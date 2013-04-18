package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.grails.Grails
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService

class KikuyuPresenterImpl implements KikuyuPresenter {

    @Override
    NamedColumnContainer getTableDataSource() {
        return new NamedColumnContainer<UrlMapping>(Grails.get(UrlMappingService).listUrlMappings(), UrlMapping.class, "pattern", "page", "matchOrder")
    }

    @Override
    List<Page> listPageOptions() {
        return Grails.get(PageService).listPages()
    }

    @Override
    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping, Container container) {
        Grails.get(UrlMappingService).switchMatchOrder firstUrlMapping, secondUrlMapping
        container.sort(["matchOrder"] as Object[], [true] as boolean[])
    }

    @Override
    void saveRow(UrlMapping urlMapping) {
        Grails.get(UrlMappingService).saveUrlMapping(urlMapping)
    }
}
