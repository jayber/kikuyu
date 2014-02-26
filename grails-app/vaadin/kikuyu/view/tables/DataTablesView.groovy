package kikuyu.view.tables

import com.vaadin.event.ItemClickEvent
import com.vaadin.grails.Grails
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.ThemeResource
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo
import kikuyu.view.KikuyuPresenter

class DataTablesView extends VerticalLayout implements View {

    KikuyuPresenter presenter

    private Table pageTable

    private Table urlMappingTable

    DataTablesView(KikuyuPresenter presenter) {
        this.presenter = presenter
        spacing = true
        setMargin(true)

        addComponent(buildTabs())
        addComponent(buildExportComponent())
    }

    private Component buildExportComponent() {
        def layout = new HorizontalLayout()
        TextField field = new TextField()
        field.setRequiredError("you must enter a valid server file path")
        field.required = true
        layout.addComponent(field)
        Button button = new Button("Export", {presenter.exportConfiguration(field)} as Button.ClickListener)
        layout.addComponent(button)
        field.setInputPrompt("export file path")
        field.setWidth("300px")
        layout.setSpacing(true)
        layout.setMargin(true)
        return layout
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent event) {
        pageTable.containerDataSource = presenter.pageTableDataSource
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
        pageTable.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
            @Override
            Object generateCell(Table source, Object itemId, Object columnId) {
                final Button removeButton = new Button("", { presenter.deletePage(source, itemId) } as Button.ClickListener)
                removeButton.setStyleName(Runo.BUTTON_LINK)
                removeButton.setIcon(new ThemeResource("cross.png"))
                removeButton.description = "delete"
                return removeButton
            }
        })
        pageTable.setColumnWidth("Delete", 50)

        pageTable.addItemClickListener(presenter.pageTableEventAction as ItemClickEvent.ItemClickListener)

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
        urlMappingTable.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
            @Override
            Object generateCell(Table source, Object itemId, Object columnId) {
                final Button removeButton = new Button("", { presenter.deleteUrlMapping(source, itemId) } as Button.ClickListener)
                removeButton.setStyleName(Runo.BUTTON_LINK)
                removeButton.setIcon(new ThemeResource("cross.png"))
                removeButton.description = "delete"
                return removeButton
            }
        })
        urlMappingTable.setColumnWidth("Delete", 50)

        urlMappingTable.addItemClickListener({ ItemClickEvent event ->
            presenter.handleUrlMappingTableClick(event, factory, urlMappingTable)
        } as ItemClickEvent.ItemClickListener)

        layout.addComponent(urlMappingTable)
        final Button button = new Button("new", { presenter.createNewUrlMapping(urlMappingTable, factory) } as Button.ClickListener)
        layout.addComponent(button)
        sheet.addTab(layout, Grails.i18n("default.urlMappingTab.label"))
    }
}
