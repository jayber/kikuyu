package kikuyu.view

import com.vaadin.event.ItemClickEvent
import com.vaadin.grails.Grails
import com.vaadin.ui.*
import com.vaadin.ui.themes.Runo

class KikuyuComponent extends VerticalLayout {

    KikuyuPresenter presenter

    KikuyuComponent(KikuyuPresenter presenter) {
        this.presenter = presenter
        setSpacing(true)
        setMargin(true)
        buildComponents()
    }

    private buildComponents() {
        final header = buildHeader()
        addComponent header
        setExpandRatio(header, 0)
        final body = buildBody()
        addComponent body
        setExpandRatio(body, 1)
    }

    private buildHeader() {
        def titleBar = new VerticalLayout()

        titleBar.addComponent(buildPageTitle())

        return titleBar
    }

    private Component buildPageTitle() {
        String homeLabel = Grails.i18n("default.home.label")
        Label label = new Label(homeLabel)
        label.styleName = Runo.LABEL_H1
        return label
    }

    private Component buildBody() {
        final layout = new VerticalLayout()
        layout.spacing = true

        layout.addComponent(buildDescription())

        layout.addComponent(buildTabs())

        return layout
    }

    private Component buildDescription() {
        String homeLabel = Grails.i18n("default.description.label")
        Label label = new Label(homeLabel)
        return label
    }

    private Component buildTabs() {
        final sheet = new TabSheet()
        Table urlMappingTable = buildUrlMappingTable()
        final tab = sheet.addTab(urlMappingTable, Grails.i18n("default.urlMappingTab.label"))
        urlMappingTable.setSizeFull()
        return sheet
    }

    private Table buildUrlMappingTable() {
        final Table urlMappingTable = new Table()
        final factory = new UrlMappingTableFieldFactory(presenter: presenter)
        urlMappingTable.setTableFieldFactory(factory)
        urlMappingTable.setContainerDataSource(presenter.tableDataSource)

        urlMappingTable.addItemClickListener({ ItemClickEvent event ->
            tableClickEventHandler(event, factory, urlMappingTable)
        } as ItemClickEvent.ItemClickListener)
        return urlMappingTable
    }

    def tableClickEventHandler = { event, factory, urlMappingTable ->
        if (event.isDoubleClick()) {
            factory.currentSelectedItemId = event.itemId
            urlMappingTable.setEditable(true)
        }
    }
}
