import kikuyu.view.HtmlRetriever
import kikuyu.view.KikuyuPresenterImpl
import kikuyu.view.util.UrlSymbolResolver

// Place your Spring DSL code here
beans = {
    xmlns util: "http://www.springframework.org/schema/util"

    kikuyuPresenter(KikuyuPresenterImpl) {
        urlMappingService = ref("urlMappingService")
        pageService = ref("pageService")
        retriever = ref("htmlRetriever")
        urlSymbolResolver = ref("urlSymbolResolver")
    }

    urlSymbolResolver(UrlSymbolResolver) {
        urlSymbolProperties = ref("urlSymbolProperties")
    }

    htmlRetriever(HtmlRetriever)

    //use -DcomponentUrlSymbolLocation to set location of properties file for each env via command line
    util.properties(id: 'urlSymbolProperties', location: "${overrideFromCommandLine('componentUrlSymbolLocation', 'classpath:componentUrlSymbolProperties.properties')}")
}

def overrideFromCommandLine(String varName, String defaultValue) {
    String overrideValue = System.getProperty(varName)
    if (overrideValue) {
        log.info "overriding $varName with command line value: $overrideValue"
        return overrideValue
    }
    log.info "using default value for: $varName = $defaultValue"
    return defaultValue
}