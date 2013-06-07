package kikuyu

import grails.converters.JSON
import kikuyu.domain.PageComponent
import kikuyu.domain.UrlMapping
import kikuyu.util.UrlSymbolResolver

class UrlMappingsController {
    UrlSymbolResolver urlSymbolResolver

    def GET() {
        JSON.use('deep')
        JSON.registerObjectMarshaller(PageComponent) { PageComponent it ->
            def returnArray = [:]

            returnArray['template'] = it.template
            returnArray['acceptPost'] = it.acceptPost
            returnArray['url'] = urlSymbolResolver.resolveToConcreteUrl(it.url)
            returnArray['slots'] = it.slots
            returnArray['substitutionVariables'] = it.substitutionVariables

            return returnArray
        }
        render(contentType: "application/json", text: UrlMapping.listOrderByMatchOrder().encodeAsJSON())
    }
}
