package kikuyu.domain

import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Page)
class PageTests {

    void testToString() {
        final Page page = new Page(name: "test")
        assert page.toString() == "test"
    }
}
