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

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o?.class) return false

        Page page = (Page) o

        if (id != page.id) return false

        return true
    }

    int hashCode() {
        return (id != null ? id.hashCode() : 0)
    }
}
