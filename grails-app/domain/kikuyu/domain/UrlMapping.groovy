package kikuyu.domain

class UrlMapping {
    static mapWith = "mongo"

    static constraints = {
        pattern nullable: false
        page nullable: true
        pageId nullable: true
        matchOrder unique: true, nullable: true
    }

    String pattern
    Page page
    BigInteger matchOrder

    @Override
    String toString() {
        "$pattern: $matchOrder"
    }

//  todo: all this is here just because the loading of page associations didn't seem to work by default
    Long pageId

    UrlMapping(String pattern, Page page) {
        this.pattern = pattern
        this.page = page
    }

    UrlMapping(String pattern, BigInteger matchOrder, Page page) {
        this.pattern = pattern
        this.page = page
        this.matchOrder = matchOrder
    }

    UrlMapping(BigInteger matchOrder, String pattern) {
        this.pattern = pattern
        this.matchOrder = matchOrder
    }

    UrlMapping(Map map) {
        map?.each { k, v ->
            if (this.hasProperty(k)) {
                this[k] = v
            }
        }
        pageId = page.id
    }

    void setPage(Page page) {
        pageId = page.id
    }

    Page getPage() {
        Page.get(pageId)
    }
}
