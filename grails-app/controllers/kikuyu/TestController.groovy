package kikuyu

class TestController {

    def component() {
        render "component text"
    }

    def template() {
        render "template <div location>something</div> text"
    }
}
