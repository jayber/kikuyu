package kikuyu

import kikuyu.domain.UrlMapping
import kikuyu.service.UrlMappingService

class UrlMappingController {
    UrlMappingService urlMappingService

    static scaffold = UrlMapping

    def GET(String pattern) {
        render urlMappingService.findByPattern(pattern)?.page?.url
    }
}
