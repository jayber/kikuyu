package kikuyu.domain

class PageComponent {

    static mapWith = "mongo"

    static constraints = {
        url nullable: true, blank: true
    }

    String url
}
