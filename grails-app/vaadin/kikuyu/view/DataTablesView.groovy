package kikuyu.view

import com.vaadin.event.ItemClickEvent
import com.vaadin.grails.Grails
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Component
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout

class DataTablesView extends VerticalLayout implements View {

    KikuyuPresenter presenter

    private Table pageTable

    private Table urlMappingTable

    DataTablesView(KikuyuPresenter presenter) {
        this.presenter = presenter
        spacing = true
        setMargin(true)

        addComponent(buildTabs())
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        pageTable.markAsDirtyRecursive()
        urlMappingTable.markAsDirtyRecursive()
    }

    private Component buildTabs() {
        final sheet = new TabSheet()
        buildUrlMappingTable(sheet)
        buildPageTable(sheet)
        return sheet
    }

    private buildPageTable(TabSheet sheet) {
        pageTable = new Table()
        pageTable.setSizeFull()
        pageTable.setContainerDataSource(presenter.pageTableDataSource)

        pageTable.addItemClickListener({ ItemClickEvent event ->
            presenter.handlePageTableEvent(event)
        } as ItemClickEvent.ItemClickListener)

        sheet.addTab(pageTable, Grails.i18n("default.pageTab.label"))
    }

    private Table buildUrlMappingTable(TabSheet sheet) {
        urlMappingTable = new Table()
        urlMappingTable.setSizeFull()
        final factory = new UrlMappingTableFieldFactory(presenter: presenter)
        urlMappingTable.setTableFieldFactory(factory)
        urlMappingTable.setContainerDataSource(presenter.urlMappingTableDataSource)

        urlMappingTable.addItemClickListener({ ItemClickEvent event ->
            presenter.handleUrlMappingTableClick(event, factory, urlMappingTable)
        } as ItemClickEvent.ItemClickListener)

        sheet.addTab(urlMappingTable, Grails.i18n("default.urlMappingTab.label"))
        return urlMappingTable
    }
}
