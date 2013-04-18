import kikuyu.view.KikuyuPresenterImpl

// Place your Spring DSL code here
beans = {
    kikuyuPresenter(KikuyuPresenterImpl) {

        urlMappingService = ref("urlMappingService")
        pageService = ref("pageService")
    }
}
