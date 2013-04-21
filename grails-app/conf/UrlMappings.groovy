class UrlMappings {

    static mappings = {

        "/ws/urlMapping/$pattern/page/url"(controller: "urlMapping") {
            action = [GET: "GET"]
        }
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(view: "/index")
        "500"(view: '/error')
    }
}