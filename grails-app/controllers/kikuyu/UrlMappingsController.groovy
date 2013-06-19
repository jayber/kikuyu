package kikuyu

import grails.converters.JSON
import kikuyu.domain.PageComponent
import kikuyu.domain.UrlMapping
import kikuyu.util.UrlSymbolResolver

class UrlMappingsController {
    UrlSymbolResolver urlSymbolResolver

    def GET() {

        JSON.use('deep')
        //todo: worried that registeringObjectMarshaller on each request will keep adding until memory fills up?
        JSON.registerObjectMarshaller(PageComponent) { PageComponent it ->
            def map = [:]

            map['template'] = it.template
            map['acceptPost'] = it.acceptPost
            map['url'] = urlSymbolResolver.resolveToConcreteUrl(it.url)
            map['slots'] = it.slots
            map['substitutionVariables'] = it.substitutionVariables

            return map
        }
        render(contentType: "application/json", text: UrlMapping.listOrderByMatchOrder().encodeAsJSON())
    }
}
