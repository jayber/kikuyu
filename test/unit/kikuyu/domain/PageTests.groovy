package kikuyu.domain

import grails.test.mixin.TestFor

@TestFor(Page)
class PageTests {

    void testToString() {
        final Page page = new Page(name: "test")
        assert page.toString() == "test"
    }
}
