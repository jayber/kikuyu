package kikuyu.service

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.UrlMapping

@TestFor(UrlMappingService)
@TestMixin(GrailsUnitTestMixin)
class UrlMappingServiceSaveUrlMappingTest {


    public void testSaveUrlMapping() throws Exception {
        def mock = mockFor(UrlMapping)
        mock.demand.save() { Map args -> }

        service.saveUrlMapping(mock.createMock())

        mock.verify()

    }
}
