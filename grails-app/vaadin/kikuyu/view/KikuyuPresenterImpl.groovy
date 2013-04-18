package kikuyu.view

import com.vaadin.data.Container
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService

class KikuyuPresenterImpl implements KikuyuPresenter {

    UrlMappingService urlMappingService
    PageService pageService

    @Override
    NamedColumnContainer getTableDataSource() {
        final container = new NamedColumnContainer<UrlMapping>(urlMappingService.listUrlMappings(),
                UrlMapping.class, "pattern", "page", "matchOrder")
        container.sort(["matchOrder"] as Object[], [true] as boolean[])
        return container
    }

    @Override
    List<Page> listPageOptions() {
        return pageService.listPages()
    }

    @Override
    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping, Container container) {
        urlMappingService.switchMatchOrder firstUrlMapping, secondUrlMapping
        container.sort(["matchOrder"] as Object[], [true] as boolean[])
    }

    @Override
    void saveRow(UrlMapping urlMapping) {
        urlMappingService.saveUrlMapping(urlMapping)
    }
}
