import kikuyu.domain.Page
import kikuyu.domain.UrlMapping

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                new UrlMapping(pattern: "/*", matchOrder: 1).save(failOnError: true)
                new UrlMapping(pattern: "/all", matchOrder: 0).save(failOnError: true)
                new Page(name: "test").save(failOnError: true)
            }
        }
    }
    def destroy = {
    }
}
