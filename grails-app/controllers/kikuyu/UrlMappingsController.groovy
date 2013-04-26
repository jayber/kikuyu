package kikuyu

import grails.converters.JSON
import kikuyu.domain.UrlMapping

class UrlMappingsController {

    def GET() {
        JSON.use('deep')
        render UrlMapping.listOrderByMatchOrder().encodeAsJSON()
    }
}
