package kikuyu.view.util

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class MapPropertyTest {

    public void testSet() throws Exception {
        HashMap map = new HashMap()
        String propertyName = 'value'
        MapProperty property = new MapProperty(bean: map, propertyName: propertyName)

        property.value = "testVal"
        assert "testVal" == map.get(propertyName)
    }

    public void testGet() throws Exception {
        HashMap map = new HashMap()
        String propertyName = 'value'
        MapProperty property = new MapProperty(bean: map, propertyName: propertyName)

        map[propertyName] = "testVal"
        assert "testVal" == property.value
    }

    public void testClass() throws Exception {

        MapProperty property = new MapProperty(propertyClass: String.class)

        assert String.class == property.getType()

    }
}
