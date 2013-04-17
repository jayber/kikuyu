package kikuyu.domain

class UrlMapping {

    static constraints = {
        pattern nullable: false
        page nullable: true
        matchOrder unique: true, nullable: true
    }

    static mapping = {
        page fetch: 'join'
    }

    String pattern
    Page page
    BigInteger matchOrder

    @Override
    String toString() {
        "$pattern: $matchOrder"
    }
}
