package kikuyu.view

import com.vaadin.data.util.BeanItemContainer

class NamedColumnContainer<Y> extends BeanItemContainer {
    private ArrayList<String> propertyIds

    NamedColumnContainer(List<Y> list, Class clazz, String... columnFields) {
        super(clazz, list)
        propertyIds = columnFields
    }

    @Override
    Collection<String> getContainerPropertyIds() {
        return propertyIds
    }

}
