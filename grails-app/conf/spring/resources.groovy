import kikuyu.view.HtmlRetriever
import kikuyu.view.KikuyuPresenterImpl

// Place your Spring DSL code here
beans = {
    xmlns util: "http://www.springframework.org/schema/util"

    kikuyuPresenter(KikuyuPresenterImpl) {
        urlMappingService = ref("urlMappingService")
        pageService = ref("pageService")
        retriever = ref("htmlRetriever")
        componentUrlSymbolProperties = ref("componentUrlSymbolProperties")
    }

    htmlRetriever(HtmlRetriever)

    //use property override configuration to change these properties in diff envs
    util.properties(id: 'componentUrlSymbolProperties', location: "${overrideFromCommandLine('componentUrlSymbolLocation', 'classpath:componentUrlSymbolProperties.properties')}")
}

def overrideFromCommandLine(String varName, String defaultValue) {
    String overrideValue = System.getProperty(varName)
    if (overrideValue) {
        log.info "overriding $varName with command line value: $overrideValue"
        return overrideValue
    }
    return defaultValue
}