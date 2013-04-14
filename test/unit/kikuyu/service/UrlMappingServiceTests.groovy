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

    @Before
    public void setUp() throws Exception {
        mock = mockDomain(UrlMapping)
    }

    void testEmptyUrlMappings() {
        assert service.listUrlMappings() == []
    }

    void testTwoUrlMappings() {
        final a = new UrlMapping(pattern: "a", matchOrder: 1)
        final b = new UrlMapping(pattern: "b", matchOrder: 0)
        mockDomain(UrlMapping, [a, b])
        final result = service.listUrlMappings()
        assert result[0] == b
        assert result[1] == a
    }
}
