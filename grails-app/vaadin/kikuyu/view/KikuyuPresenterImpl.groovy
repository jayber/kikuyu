package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.event.ItemClickEvent
import com.vaadin.navigator.Navigator
import com.vaadin.ui.Table
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService
import kikuyu.util.UrlSymbolResolver
import kikuyu.view.editpage.EditPageView
import kikuyu.view.editpage.NamePrompt
import kikuyu.view.editpage.SinglePageComponent
import kikuyu.view.tables.DataTablesView
import kikuyu.view.tables.NamedColumnContainer
import kikuyu.view.tables.UrlMappingTableFieldFactory

import java.util.regex.Matcher
import java.util.regex.Pattern

class KikuyuPresenterImpl implements KikuyuPresenter {

    UrlSymbolResolver urlSymbolResolver
    Pattern slotPattern = ~"<div[^<>]*?location\\s*?>.*?</\\s*?div>"
    Pattern substVarPattern = ~/@\{(.*?)\}/

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
        return new NamedColumnContainer<UrlMapping>(pageService.listPages(), Page, "name")
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
        final DataTablesView tablesView = new DataTablesView(this)
        navigator.addView("", tablesView)
    }

    def pageTableEventAction = { ItemClickEvent event ->
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

    def navigateHomeAction = {
        navigator.navigateTo("")
    }

    def scanAction = { SinglePageComponent pageComponent ->
        final int slots = acquireNumberOfSlots(urlSymbolResolver.resolveToConcreteUrl(pageComponent.getUrl()))
        final String[] varNames = acquireSubstitutionVarNames(urlSymbolResolver.resolveToConcreteUrl(pageComponent.getUrl()))
        pageComponent.slots = slots

        def vars = [:]
        for (String name : varNames) {
            vars.put(name, "")
        }
        pageComponent.substitutionVariables = vars

        pageComponent.container.makeSlots()
        savePage(pageComponent.container.page)
    }

    def removeAction = { SinglePageComponent pageComponent ->
        pageComponent.container.removePageComponent(pageComponent)
        savePage(pageComponent.container.page)
    }

    @Override
    def deleteUrlMapping(Table source, UrlMapping urlMapping) {
        source.removeItem(urlMapping)
        urlMappingService.deleteMapping(urlMapping)
    }

    @Override
    def deletePage(Table table, Page page) {
        table.removeItem(page)
        pageService.deletePage(page)
    }

    @Override
    def addSubstitutionVar(SinglePageComponent component) {
        final Window window

        final NamePrompt prompt = new NamePrompt({ String value ->
            component.addSubstitutionVariable(value)
            component.getUI().removeWindow(window)
        })
        window = new Window("Name for variable", prompt)
        window.modal = true
        component.getUI().addWindow(window)
    }
}
