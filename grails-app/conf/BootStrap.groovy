import kikuyu.domain.Page
import kikuyu.domain.UrlMapping

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                new UrlMapping(pattern: "/*", matchOrder: 1).save(failOnError: true)
                new UrlMapping(pattern: "/all", matchOrder: 0).save(failOnError: true)
                new UrlMapping(pattern: "/spock", matchOrder: 2).save(failOnError: true)
                new UrlMapping(pattern: "/kirk", matchOrder: 3).save(failOnError: true)
                new UrlMapping(pattern: "/sulu", matchOrder: 4).save(failOnError: true)
                new UrlMapping(pattern: "/chekov", matchOrder: 5).save(failOnError: true)
                new UrlMapping(pattern: "/uhuru", matchOrder: 6).save(failOnError: true)
                new UrlMapping(pattern: "/crusher", matchOrder: 7).save(failOnError: true)
                new UrlMapping(pattern: "/scotty", matchOrder: 8).save(failOnError: true)
                new UrlMapping(pattern: "/bones", matchOrder: 9).save(failOnError: true)
                new UrlMapping(pattern: "/labrador", matchOrder: 10).save(failOnError: true)
                new UrlMapping(pattern: "/whippet", matchOrder: 11).save(failOnError: true)
                new Page(name: "test").save(failOnError: true)
            }
        }
    }
    def destroy = {
    }
}
