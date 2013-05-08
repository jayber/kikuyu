package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.event.ItemClickEvent
import com.vaadin.navigator.Navigator
import com.vaadin.ui.Table
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping

public interface KikuyuPresenter {

    Container getUrlMappingTableDataSource()

    Container getPageTableDataSource()

    List<Page> listPageOptions()

    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping, Container component)

    void saveRow(UrlMapping urlMapping)

    void handleUrlMappingTableClick(def event, UrlMappingTableFieldFactory fieldFactory, Table table)

    void setNavigator(Navigator navigator)

    Navigator getNavigator()

    void showEditPage(Page page)

    void savePage(Page page)

    void buildNavigator(VerticalLayout layout, UI ui)

    void handlePageTableEvent(ItemClickEvent event)

    int acquireNumberOfSlots(String templateUrl)

    def getCreateNewPage()

    String[] acquireSubstitutionVarNames(String componentUrl)
}