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
            new UrlMapping(matchOrder: 0, pattern: "pattern0"),
            new UrlMapping(matchOrder: 1, pattern: "pattern1"),
            new UrlMapping(matchOrder: 2, pattern: "pattern2")
    ]
}
