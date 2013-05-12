package util

import com.vaadin.data.Property
import com.vaadin.data.util.AbstractProperty

class MapProperty extends AbstractProperty {
    Map bean
    String propertyName
    Class propertyClass

    @Override
    Object getValue() {
        return bean[propertyName]
    }

    @Override
    void setValue(Object newValue) throws Property.ReadOnlyException {
        bean[propertyName] = newValue
    }

    @Override
    Class getType() {
        return propertyClass
    }
}
