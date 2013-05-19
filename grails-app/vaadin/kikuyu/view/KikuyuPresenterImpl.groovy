package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.event.ItemClickEvent
import com.vaadin.navigator.Navigator
import com.vaadin.ui.Table
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService

import java.util.regex.Matcher
import java.util.regex.Pattern

class KikuyuPresenterImpl implements KikuyuPresenter {

    Pattern slotPattern = ~"<div[^<>]*?location\\s*?>.*?</\\s*?div>"
    Pattern substVarPattern = ~/#\{(.*?)\}/

    UrlMappingService urlMappingService
    PageService pageService
    Navigator navigator
    HtmlRetriever retriever

    @Override
    NamedColumnContainer getUrlMappingTableDataSource() {
        final container = new NamedColumnContainer<UrlMapping>(urlMappingService.listUrlMappings(),
                UrlMapping.class, "pattern", "page", "matchOrder")
        container.sort(["matchOrder"] as Object[], [true] as boolean[])
        return container
    }

    @Override
    Container getPageTableDataSource() {
        return new NamedColumnContainer<UrlMapping>(pageService.listPages(),
                Page, "name")
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

    @Override
    void handleUrlMappingTableClick(def event, UrlMappingTableFieldFactory factory, Table urlMappingTable) {
        if (event.isDoubleClick()) {
            factory.currentSelectedItemId = event.itemId
            urlMappingTable.setEditable(true)
        } else {
            urlMappingTable.setEditable(false)
        }
    }

    @Override
    void showEditPage(Page page) {
        final String viewName = "pageEditor-" + page.id
        navigator.addView(viewName, new EditPageView(this, page))
        navigator.navigateTo(viewName)
    }

    @Override
    void savePage(Page page) {
        pageService.savePage(page)
    }

    @Override
    void buildNavigator(VerticalLayout layout, UI ui) {
        def navigator = new Navigator(ui, layout)
        this.navigator = navigator
        navigator.addView("", new DataTablesView(this))
    }

    @Override
    void handlePageTableEvent(ItemClickEvent event) {
        if (event.isDoubleClick()) {
            showEditPage(event.itemId)
        }
    }

    @Override
    int acquireNumberOfSlots(String templateUrl) {
        String templateHtml = retriever.retrieveHtml(templateUrl)
        final Matcher matcher = slotPattern.matcher(templateHtml)
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    def createNewPage = {
        showEditPage(new Page())
    }

    def createNewUrlMapping = { urlMappingTable, factory ->
        UrlMapping mapping = new UrlMapping("new pattern", null)
        mapping.matchOrder = urlMappingService.findLastMatchOrder() + 1
        urlMappingTable.addItem(mapping);
        factory.currentSelectedItemId = mapping
        urlMappingTable.setEditable(true)
    }

    @Override
    String[] acquireSubstitutionVarNames(String componentUrl) {
        String templateHtml = retriever.retrieveHtml(componentUrl)
        final Matcher matcher = substVarPattern.matcher(templateHtml)

        List<String> result = []
        while (matcher.find()) {
            if (matcher.groupCount() == 1) {
                result.add(matcher.group(1))
            }
        }
        return result
    }
}
