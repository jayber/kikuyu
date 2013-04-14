package kikuyu.view

import com.vaadin.data.Container
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping

public interface KikuyuPresenter {
    Container getTableDataSource()

    List<Page> listPageOptions()

    void incrementMatchOrder(UrlMapping urlMapping)

    void decrementMatchOrder(UrlMapping urlMapping)
}