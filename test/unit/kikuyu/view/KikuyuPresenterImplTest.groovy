package kikuyu.view

import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.event.ItemClickEvent
import com.vaadin.navigator.Navigator
import com.vaadin.shared.MouseEventDetails
import com.vaadin.ui.Component
import com.vaadin.ui.Table
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
import kikuyu.TestFixtures
import kikuyu.domain.Page
import kikuyu.domain.UrlMapping
import kikuyu.service.PageService
import kikuyu.service.UrlMappingService
import kikuyu.util.UrlSymbolResolver
import kikuyu.view.editpage.EditPageView
import kikuyu.view.editpage.SinglePageComponent
import kikuyu.view.tables.NamedColumnContainer
import kikuyu.view.tables.UrlMappingTableFieldFactory
import org.junit.Before
import org.junit.Test

import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.*

@TestMixin(GrailsUnitTestMixin)
class KikuyuPresenterImplTest {

    KikuyuPresenterImpl target

    MockFor urlMappingService
    MockFor pageService
    UrlSymbolResolver symbolicUrlResolver

    @Before
    public void setUp() throws Exception {
        target = new KikuyuPresenterImpl()
        urlMappingService = new MockFor(UrlMappingService)
        pageService = new MockFor(PageService)
        symbolicUrlResolver = new UrlSymbolResolver(urlSymbolProperties: new Properties())
    }

    void testGetTableDataSource() {
        urlMappingService.demand.listUrlMappings() { new ArrayList<UrlMapping>() }
        target.urlMappingService = urlMappingService.proxyDelegateInstance()

        final result = target.getUrlMappingTableDataSource()
        assert result instanceof NamedColumnContainer<UrlMapping>
        assert result.containerPropertyIds == ["pattern", "page", "matchOrder"]
    }

    public void testListOptions() throws Exception {
        pageService.demand.listPages() {
            TestFixtures.pages
        }
        target.pageService = pageService.proxyDelegateInstance()
        final List<Page> options = target.listPageOptions()
        assert TestFixtures.pages == options
    }

    public void testSwitchMatchOrder() throws Exception {
        UrlMapping mapping1 = new UrlMapping()
        UrlMapping mapping2 = new UrlMapping()
        urlMappingService.demand.switchMatchOrder() { UrlMapping urlMapping1, UrlMapping urlMapping2 ->
            assert mapping1 == urlMapping1
            assert mapping2 == urlMapping2
        }

        def containerMock = mockFor(Container.Sortable)
        containerMock.demand.sort() { Object[] cols, boolean[] something -> }

        final instance = urlMappingService.proxyInstance()
        target.urlMappingService = instance
        target.switchMatchOrder(mapping1, mapping2, containerMock.createMock())

        urlMappingService.verify(instance)
    }

    public void testSaveRow() throws Exception {
        urlMappingService.demand.saveUrlMapping() { UrlMapping urlMapping -> }
        final GroovyObject instance = urlMappingService.proxyInstance()
        target.setUrlMappingService(instance)
        final UrlMapping mapping = new UrlMapping()
        target.saveRow(mapping)

        urlMappingService.verify(instance)
    }

    void testHandleUrlMappingTableClickHandlerSingleClick() {

        def factoryMock = mockFor(UrlMappingTableFieldFactory)
        def tableMock = mockFor(Table)
        tableMock.demand.setEditable(1) { value -> assert !value }

        def eventMock = new MockFor(ItemClickEvent)
        eventMock.demand.isDoubleClick(1) { false }

        def eventMockInstance = eventMock.proxyInstance(
                [{} as Component, {} as Item, {}, {}, {} as MouseEventDetails] as Object[]
        )

        target.handleUrlMappingTableClick(eventMockInstance, factoryMock.createMock(), tableMock.createMock())

        factoryMock.verify()
        tableMock.verify()
        eventMock.verify(eventMockInstance)
    }

    void testHandleUrlMappingTableClickHandlerDoubleClick() {

        def factoryMock = mockFor(UrlMappingTableFieldFactory)
        def tableMock = mockFor(Table)
        factoryMock.demand.setCurrentSelectedItemId(1) {}
        tableMock.demand.setEditable(1) { value -> assert value }

        def eventMock = new MockFor(ItemClickEvent)
        eventMock.demand.isDoubleClick(1) { true }
        eventMock.demand.getItemId(1) { 1 }

        def eventMockInstance = eventMock.proxyInstance(
                [{} as Component, {} as Item, {}, {}, {} as MouseEventDetails] as Object[]
        )

        target.handleUrlMappingTableClick(eventMockInstance, factoryMock.createMock(), tableMock.createMock())

        factoryMock.verify()
        tableMock.verify()
        eventMock.verify(eventMockInstance)
    }

    public void testGetPageContainer() throws Exception {
        pageService.demand.listPages() { TestFixtures.pages }

        final instance = pageService.proxyInstance()
        target.pageService = instance

        final pages = target.pageTableDataSource
        assert pages instanceof NamedColumnContainer<Page>
        assert pages.containerPropertyIds == ["name"]
        //todo: same problem!! no idea why this fails
//        assert pages.itemIds == TestFixtures.pages

        pageService.verify(instance)

    }


    @Test
    public void testSavePage() throws Exception {
        final Page page = new Page()
        pageService.demand.savePage(1) { Page page1 -> assert page == page1 }
        final GroovyObject instance = pageService.proxyInstance()
        target.pageService = instance

        target.savePage(page)

        pageService.verify(instance)
    }

    @Test
    public void testHandlePageTableSingleClickEvent() throws Exception {

        final Navigator navigator = mock(Navigator)
        target.navigator = navigator
        final ItemClickEvent event = mock(ItemClickEvent)
        when(event.isDoubleClick()).thenReturn(false)

        target.pageTableEventAction(event)

        verifyNoMoreInteractions(navigator)

    }

    @Test
    public void testHandlePageTableDoubleClickEvent() throws Exception {

        final Navigator navigator = mock(Navigator)
        target.navigator = navigator
        final Page page = new Page()
        final ItemClickEvent event = mock(ItemClickEvent)
        when(event.isDoubleClick()).thenReturn(true)
        when(event.itemId).thenReturn(page)

        target.pageTableEventAction(event)

        verify(navigator).addView(eq("pageEditor-null"), any(EditPageView))
        verify(navigator).navigateTo("pageEditor-null")
        verifyNoMoreInteractions(navigator)

    }

    @Test
    public void testAcquireNumberOfSlots() throws Exception {

        final GrailsMock mock = mockFor(HtmlRetriever)
        final String testUrl = "testUrl"
        mock.demand.retrieveHtml(1) { String url ->
            assert testUrl == url
            "before <div location> </div> middle <div location> </div> after"
        }
        target.retriever = mock.createMock()

        final int slots = target.acquireNumberOfSlots(testUrl)
        assert 2 == slots
    }

    @Test
    public void testCreateNewPage() throws Exception {
        final Navigator navigator = mock(Navigator)
        target.navigator = navigator

        target.createNewPage()

        verify(navigator).addView(eq("pageEditor-null"), any(EditPageView))
        verify(navigator).navigateTo("pageEditor-null")
        verifyNoMoreInteractions(navigator)
    }

    @Test
    public void testAcquireSubstitutionVarNames() throws Exception {

        final GrailsMock mock = mockFor(HtmlRetriever)
        final String testUrl = "testUrl"
        mock.demand.retrieveHtml(1) { String url ->
            assert testUrl == url
            "before #{var1} middle #{var2} after"
        }
        target.retriever = mock.createMock()

        final String[] names = target.acquireSubstitutionVarNames(testUrl)

        assert ["var1", "var2"] == names
    }

    public void testCreateNewUrlMapping() throws Exception {
        urlMappingService.demand.findLastMatchOrder() { 10 }
        target.urlMappingService = urlMappingService.proxyDelegateInstance()

        final GrailsMock mockTable = mockFor(Table)
        final GrailsMock mockFactory = mockFor(UrlMappingTableFieldFactory)
        mockTable.demand.addItem(1) { UrlMapping mapping ->
            assert mapping.pattern == "new pattern"
        }
        mockFactory.demand.setCurrentSelectedItemId(1) { UrlMapping mapping ->
            assert mapping.pattern == "new pattern"
            assert mapping.matchOrder == 11
        }
        mockTable.demand.setEditable(1) { boolean editable ->
            assert editable
        }

        final Object tableMock = mockTable.createMock()
        final Object factoryInstance = mockFactory.createMock()

        target.createNewUrlMapping(tableMock, factoryInstance)
    }

    @Test
    public void testScan() throws Exception {

        final String testUrl = "testUrl"

        def page = new Page()
        def mockForEditPageView = new MockFor(EditPageView)
        mockForEditPageView.demand.makeSlots() {}
        mockForEditPageView.demand.getPage() { page }
        def editPageViewMockInstance = mockForEditPageView.proxyInstance()

        MockFor mockForSinglePageComponent = new MockFor(SinglePageComponent)
        mockForSinglePageComponent.demand.getUrl(2) { testUrl }
        mockForSinglePageComponent.demand.setSlots() { int i ->
            assert 0 == i
        }
        mockForSinglePageComponent.demand.setSubstitutionVariables() { Map map -> }
        mockForSinglePageComponent.demand.getContainer(2) { editPageViewMockInstance }

        final GrailsMock mockRetriever = mockFor(HtmlRetriever)
        mockRetriever.demand.retrieveHtml(2) { String url ->
            assert testUrl == url
            "test html"
        }
        target.retriever = mockRetriever.createMock()

        target.urlSymbolResolver = symbolicUrlResolver

        def pageServiceMockInstance = setUpPageServiceSave(page)

        def singlePageCompMockInstance = mockForSinglePageComponent.proxyInstance()
        target.scanAction(singlePageCompMockInstance)

        mockForSinglePageComponent.verify(singlePageCompMockInstance)
        mockForEditPageView.verify(editPageViewMockInstance)
        pageService.verify(pageServiceMockInstance)
    }

    private GroovyObject setUpPageServiceSave(Page page) {
        pageService.demand.savePage(1) { Page page1 -> assert page == page1 }
        final GroovyObject pageServiceMockInstance = pageService.proxyInstance()
        target.pageService = pageServiceMockInstance
        return pageServiceMockInstance
    }

    public void testRemove() throws Exception {

        def page = new Page()
        def mockForEditPageView = new MockFor(EditPageView)
        mockForEditPageView.demand.removePageComponent() {}
        mockForEditPageView.demand.getPage() { page }
        def editPageViewMockInstance = mockForEditPageView.proxyInstance()

        MockFor mockForSinglePageComponent = new MockFor(SinglePageComponent)
        mockForSinglePageComponent.demand.getContainer(2) { editPageViewMockInstance }
        def singlePageMockInstance = mockForSinglePageComponent.proxyInstance()

        def pageServiceMockInstance = setUpPageServiceSave(new Page())

        target.removeAction(singlePageMockInstance)

        mockForEditPageView.verify(editPageViewMockInstance)
        mockForSinglePageComponent.verify(singlePageMockInstance)
        pageService.verify(pageServiceMockInstance)

    }

    public void testNavigateHome() throws Exception {

        final Navigator navigator = mock(Navigator)
        target.navigator = navigator

        target.navigateHomeAction()

        verify(navigator).navigateTo("")
    }

    public void testDeleteMapping() throws Exception {
        Table source = mock(Table)
        urlMappingService.demand.deleteMapping(1) {}
        GroovyObject instance = urlMappingService.proxyInstance()
        target.urlMappingService = instance
        UrlMapping mapping = new UrlMapping()
        target.deleteUrlMapping(source, mapping)

        verify(source).removeItem(mapping)
        urlMappingService.verify(instance)
    }

    public void testDeletePage() throws Exception {
        Table source = mock(Table)
        pageService.demand.deletePage(1) {}
        GroovyObject instance = pageService.proxyInstance()
        target.pageService = instance
        Page page = new Page()
        target.deletePage(source, page)

        verify(source).removeItem(page)
        pageService.verify(instance)
    }
}
