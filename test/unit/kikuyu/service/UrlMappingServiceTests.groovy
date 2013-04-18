package kikuyu.service

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import kikuyu.domain.UrlMapping
import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UrlMappingService)
@TestMixin(DomainClassUnitTestMixin)
class UrlMappingServiceTests {
    private mock

    def a = new UrlMapping(1, "a")
    def b = new UrlMapping(0, "b")
    private ArrayList<UrlMapping> mappings

    @Before
    public void setUp() throws Exception {
        mock = mockDomain(UrlMapping)
        mappings = [a, b]
    }

    void testEmptyUrlMappings() {
        assert service.listUrlMappings() == []
    }

    void testTwoUrlMappings() {
        mockDomain(UrlMapping, mappings)
        final result = service.listUrlMappings()
        assert result[0] == b
        assert result[1] == a
    }

    public void testSwitchMatchOrder() throws Exception {

        mockDomain(UrlMapping, mappings)
        service.switchMatchOrder(a, b)
        final result = service.listUrlMappings()
        assert result[0] == a
        assert result[1] == b
    }
}
