package kikuyu.view

import com.vaadin.ui.Table
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService

class KikuyuPresenterImpl implements KikuyuPresenter {

    UrlMappingService urlMappingService
    PageService pageService

    @Override
    NamedColumnContainer getTableDataSource() {
        return new NamedColumnContainer<UrlMapping>(urlMappingService.listUrlMappings(), UrlMapping.class, "pattern", "page", "matchOrder")
    }

    @Override
    List<Page> listPageOptions() {
        return pageService.listPages()
    }

    @Override
    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping, Table component) {
        urlMappingService.switchMatchOrder firstUrlMapping, secondUrlMapping
        refreshTableData(component)
    }

    private void refreshTableData(Table component) {
        NamedColumnContainer dataSource = component.getContainerDataSource()
        dataSource.sort(["matchOrder"] as Object[], [true] as boolean[])
    }
}
