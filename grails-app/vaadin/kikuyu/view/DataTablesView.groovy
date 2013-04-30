package kikuyu.view

import com.vaadin.event.ItemClickEvent
import com.vaadin.grails.Grails
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo

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
        final VerticalLayout layout = new VerticalLayout()
        layout.setStyleName(Runo.LAYOUT_DARKER)
        pageTable = new Table()
        pageTable.setSizeFull()
        pageTable.setContainerDataSource(presenter.pageTableDataSource)

        pageTable.addItemClickListener({ ItemClickEvent event ->
            presenter.handlePageTableEvent(event)
        } as ItemClickEvent.ItemClickListener)

        layout.addComponent(pageTable)
        final Button button = new Button("new", presenter.createNewPage as Button.ClickListener)
        layout.addComponent(button)
        sheet.addTab(layout, Grails.i18n("default.pageTab.label"))
    }

    private void buildUrlMappingTable(TabSheet sheet) {
        final VerticalLayout layout = new VerticalLayout()
        layout.setStyleName(Runo.LAYOUT_DARKER)
        urlMappingTable = new Table()
        urlMappingTable.setSizeFull()
        final factory = new UrlMappingTableFieldFactory(presenter: presenter)
        urlMappingTable.setTableFieldFactory(factory)
        urlMappingTable.setContainerDataSource(presenter.urlMappingTableDataSource)

        urlMappingTable.addItemClickListener({ ItemClickEvent event ->
            presenter.handleUrlMappingTableClick(event, factory, urlMappingTable)
        } as ItemClickEvent.ItemClickListener)

        layout.addComponent(urlMappingTable)
        final Button button = new Button("new", { presenter.createNewUrlMapping(urlMappingTable, factory) } as Button.ClickListener)
        layout.addComponent(button)
        sheet.addTab(layout, Grails.i18n("default.urlMappingTab.label"))
    }
}
