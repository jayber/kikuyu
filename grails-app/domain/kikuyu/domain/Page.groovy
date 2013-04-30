package kikuyu.domain

class Page {
    static mapWith = "mongo"

    static hasMany = [pageComponents: PageComponent]

    static constraints = {
        name nullable: false, blank: false, unique: true
    }

    static mappings = {
        pageComponents lazy: false
    }

    static embedded = ['pageComponents']

    String name
    List<PageComponent> pageComponents

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
