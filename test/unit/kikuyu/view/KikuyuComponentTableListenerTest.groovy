package kikuyu.view

import com.vaadin.data.Item
import com.vaadin.event.ItemClickEvent
import com.vaadin.shared.MouseEventDetails
import com.vaadin.ui.Component
import com.vaadin.ui.Table
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor


@TestMixin(GrailsUnitTestMixin)
class KikuyuComponentTableListenerTest {
    void testGetTableClickHandler() {

        def factoryMock = mockFor(UrlMappingTableFieldFactory)
        def tableMock = mockFor(Table)
        factoryMock.demand.setCurrentSelectedItemId(1) {}
        tableMock.demand.setEditable(1) {}

        def eventMock = new MockFor(ItemClickEvent)
        eventMock.demand.isDoubleClick(1) { true }
        eventMock.demand.getItemId(1) { 1 }

        KikuyuComponent component = new KikuyuComponent({} as KikuyuPresenter)
        final handler = component.tableClickEventHandler

        def eventMockInstance = eventMock.proxyDelegateInstance(
                [{} as Component, {} as Item, {}, {}, {} as MouseEventDetails] as Object[]
        )

        handler(eventMockInstance, factoryMock.createMock(), tableMock.createMock())

        factoryMock.verify()
        tableMock.verify()
        eventMock.verify(eventMockInstance)
    }
}
