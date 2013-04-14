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
    Container getTableDataSource() {
        return new NamedColumnContainer<UrlMapping>(urlMappingService.listUrlMappings(), UrlMapping.class, "pattern", "page", "matchOrder")
    }

    @Override
    List<Page> listPageOptions() {
        return pageService.listPages()
    }
}
