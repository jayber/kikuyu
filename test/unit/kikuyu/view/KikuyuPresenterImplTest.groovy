package kikuyu.view

import com.vaadin.data.Container
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

    MockFor urlMappingService
    MockFor pageService

    @Before
    public void setUp() throws Exception {
        target = new KikuyuPresenterImpl()
        urlMappingService = new MockFor(UrlMappingService)
        pageService = new MockFor(PageService)
    }

    void testGetTableDataSource() {
        urlMappingService.demand.listUrlMappings() { new ArrayList<UrlMapping>() }
        target.urlMappingService = urlMappingService.proxyDelegateInstance()

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

    public void testSwitchMatchOrder() throws Exception {
        UrlMapping mapping1 = new UrlMapping()
        UrlMapping mapping2 = new UrlMapping()
        urlMappingService.demand.switchMatchOrder() { UrlMapping urlMapping1, UrlMapping urlMapping2 ->
            assert mapping1 == urlMapping1
            assert mapping2 == urlMapping2
        }

        def containerMock = mockFor(Container.Sortable)
        containerMock.demand.sort() { Object[] cols, boolean[] something -> }

        final instance = urlMappingService.proxyInstance()
        target.urlMappingService = instance
        target.switchMatchOrder(mapping1, mapping2, containerMock.createMock())

        urlMappingService.verify(instance)
    }
}
