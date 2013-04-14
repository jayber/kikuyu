package kikuyu.view

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
import kikuyu.TestFixtures
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService
import org.junit.Before

@TestMixin(GrailsUnitTestMixin)
class KikuyuPresenterImplTest {

    KikuyuPresenterImpl target

    MockFor service
    MockFor pageService

    @Before
    public void setUp() throws Exception {
        target = new KikuyuPresenterImpl()
        service = new MockFor(UrlMappingService)
        pageService = new MockFor(PageService)
    }

    void testGetTableDataSource() {
        service.demand.listUrlMappings() { new ArrayList<UrlMapping>() }
        target.urlMappingService = service.proxyDelegateInstance()

        final result = target.getTableDataSource()
        assert result instanceof NamedColumnContainer<UrlMapping>
        assert result.containerPropertyIds == ["pattern", "page", "matchOrder"]
    }

    public void testListOptions() throws Exception {
        pageService.demand.listPages() {
            TestFixtures.pages
        }
        target.pageService = pageService.proxyDelegateInstance()
        target.listPageOptions()
    }
}
