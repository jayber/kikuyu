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

    DataTablesView(KikuyuPresenter presenter) {
        this.presenter = presenter
        spacing = true

        addComponent(buildTabs())

        setMargin(true)
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private Component buildTabs() {
        final sheet = new TabSheet()
        buildUrlMappingTable(sheet)
        buildPageTable(sheet)
        return sheet
    }

    private buildPageTable(TabSheet sheet) {
        final Table pageTable = new Table()
        pageTable.setSizeFull()
        pageTable.setContainerDataSource(presenter.pageTableDataSource)

        pageTable.addItemClickListener({ ItemClickEvent event ->
            presenter.showEditPage(event.itemId)
        } as ItemClickEvent.ItemClickListener)

        sheet.addTab(pageTable, Grails.i18n("default.pageTab.label"))
    }

    private Table buildUrlMappingTable(TabSheet sheet) {
        final Table urlMappingTable = new Table()
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
