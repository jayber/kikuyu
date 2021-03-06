import kikuyu.domain.Page
import kikuyu.domain.UrlMapping

//run this script with the run-script grails command to get full GORM access, i.e.:
//run-script scripts\DataLoad.groovy

def blank = new Page(name: "blank").save(failOnError: true)
def home = new Page(name: "home").save(failOnError: true)
def userAccount = new Page(name: "userAccount").save(failOnError: true)
new UrlMapping(pattern: "/*", matchOrder: 1).save(failOnError: true)
new UrlMapping(pattern: "/home", matchOrder: 0, page: home).save(failOnError: true)
new UrlMapping(pattern: "/spock", matchOrder: 2, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/kirk", matchOrder: 3, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/sulu", matchOrder: 4, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/chekov", matchOrder: 5, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/uhuru", matchOrder: 6, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/crusher", matchOrder: 7, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/scotty", matchOrder: 8, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/bones", matchOrder: 9, page: userAccount).save(failOnError: true)
new UrlMapping(pattern: "/labrador", matchOrder: 10, page: blank).save(failOnError: true)
new UrlMapping(pattern: "/whippet", matchOrder: 11, page: blank).save(failOnError: true)
