package kikuyu.service

import kikuyu.domain.Page

class PageService {

    List<Page> listPages() {
        Page.listOrderByName()
    }

    void savePage(Page page) {
        page.save(flush: true, failOnError: true)
    }
}
