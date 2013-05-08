package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.event.ItemClickEvent
import com.vaadin.navigator.Navigator
import com.vaadin.ui.Notification
import com.vaadin.ui.Table
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService
import org.springframework.web.client.RestTemplate

import java.util.regex.Matcher
import java.util.regex.Pattern

class KikuyuPresenterImpl implements KikuyuPresenter {


    Pattern slotPattern = Pattern.compile("<div[^<>]*?location\\s*?>.*?</\\s*?div>")
    Pattern substVarPattern = ~/#\{.*\}/

    UrlMappingService urlMappingService
    PageService pageService
    Navigator navigator

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
        navigator.addView("pageEditor", new EditPageView(this, page))
        navigator.navigateTo("pageEditor")
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
        String templateHtml = retrieveHtml(templateUrl)
        final Matcher matcher = slotPattern.matcher(templateHtml)
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private String retrieveHtml(String url) {
        String templateUrl
        RestTemplate restTemplate = new RestTemplate()
        final String templateHtml = restTemplate.getForObject(url, String.class)
        templateHtml
    }


    def createNewPage = {
        showEditPage(new Page())
    }

    def createNewUrlMapping = { urlMappingTable, factory ->
        final UrlMapping mapping = new UrlMapping(pattern: "new pattern")
        mapping.matchOrder = urlMappingService.findLastMatchOrder() + 1
        urlMappingTable.addItem(mapping);
        factory.currentSelectedItemId = mapping
        urlMappingTable.setEditable(true)
    }

    @Override
    String[] acquireSubstitutionVarNames(String componentUrl) {
        String templateHtml = retrieveHtml(componentUrl)
        final Matcher matcher = substVarPattern.matcher(templateHtml)
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        Notification.show(count.toString())
        return null
    }
}
