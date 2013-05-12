package kikuyu.domain

class PageComponent {

    static mapWith = "mongo"

    static constraints = {
        url nullable: true, blank: true
    }

    static mappings = {
        substitutionVariables lazy: false
    }

    static embedded = ['substitutionVariables']

    Boolean template
    Boolean acceptPost
    String url
    BigInteger slots
    Map substitutionVariables = new HashMap()
}
