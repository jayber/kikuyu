package kikuyu.view

import com.vaadin.data.Container
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping

public interface KikuyuPresenter {
    Container getTableDataSource()

    List<Page> listPageOptions()

    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping)
}