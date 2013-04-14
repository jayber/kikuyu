package kikuyu.domain

class UrlMapping {

    static constraints = {
        pattern nullable: false
        page nullable: true
    }

    String pattern
    Page page
    BigInteger matchOrder
}
