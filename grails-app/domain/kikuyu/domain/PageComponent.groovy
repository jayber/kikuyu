package kikuyu.domain

class PageComponent {

    static mapWith = "mongo"

    static constraints = {
        url nullable: true, blank: true
    }
    static hasMany = [substitutionVariables: SubstitutionVariable]

    static mappings = {
        substitutionVariables lazy: false
    }

    static embedded = ['substitutionVariables']

    Boolean template
    Boolean acceptPost
    String url
    BigInteger slots
    List<SubstitutionVariable> substitutionVariables
}
