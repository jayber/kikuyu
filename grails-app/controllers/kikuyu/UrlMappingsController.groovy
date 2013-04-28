package kikuyu

import grails.converters.JSON
import kikuyu.domain.UrlMapping

class UrlMappingsController {

    def GET() {
        JSON.use('deep')
        render(contentType: "application/json", text: UrlMapping.listOrderByMatchOrder().encodeAsJSON())
    }
}
