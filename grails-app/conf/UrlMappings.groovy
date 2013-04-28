class UrlMappings {

    static mappings = {
        "/test/$action"(controller: "testController")

        "/ws/urlMappings"(controller: "urlMappings") {
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