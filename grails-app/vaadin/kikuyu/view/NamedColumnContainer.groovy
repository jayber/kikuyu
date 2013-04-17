package kikuyu.view

import com.vaadin.data.util.BeanItemContainer
import kikuyu.domain.UrlMapping

class NamedColumnContainer<Y> extends BeanItemContainer {
    private ArrayList<String> propertyIds

    NamedColumnContainer(List<Y> list, Class clazz, String... columnFields) {
        super(clazz, list)
        propertyIds = columnFields
    }

    NamedColumnContainer() {
        super(UrlMapping)
    }

    @Override
    Collection<String> getContainerPropertyIds() {
        return propertyIds
    }

}
