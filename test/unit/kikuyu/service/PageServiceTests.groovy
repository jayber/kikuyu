package kikuyu.service

import grails.test.GrailsMock
import grails.test.mixin.TestFor
import kikuyu.domain.Page
import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PageService)
class PageServiceTests {

    private PageService service

    @Before
    public void setUp() throws Exception {
        service = new PageService()
    }

    void testSomething() {
        final GrailsMock mock = mockFor(Page)
        mock.demand.static.listOrderByName() { [] }

        service.listPages()

        mock.verify()
    }
}
