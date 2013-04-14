package kikuyu.service

import kikuyu.domain.Page

class PageService {

    List<Page> listPages() {
        Page.listOrderByName()
    }
}
