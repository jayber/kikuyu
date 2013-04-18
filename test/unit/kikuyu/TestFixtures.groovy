package kikuyu

import kikuyu.domain.Page
import kikuyu.domain.UrlMapping;

public class TestFixtures {
    public static ArrayList<Page> pages = [
            new Page(name: "page1"),
            new Page(name: "page2"),
            new Page(name: "page3")
    ]
    public static ArrayList<UrlMapping> urlMappings = [
            new UrlMapping(0, "pattern0"),
            new UrlMapping(1, "pattern1"),
            new UrlMapping(2, "pattern2")
    ]
}
