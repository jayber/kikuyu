package kikuyu.domain

class Page {

    static constraints = {
        name nullable: false, blank: false
    }
    String name

    @Override
    String toString() {
        return name
    }
}
