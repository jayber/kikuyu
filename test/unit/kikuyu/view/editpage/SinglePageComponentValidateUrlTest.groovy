package kikuyu.view.editpage

import com.vaadin.data.Property
import com.vaadin.ui.Image
import grails.test.GrailsMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kikuyu.domain.PageComponent
import kikuyu.view.KikuyuPresenter
import kikuyu.view.util.UrlSymbolResolver
import org.junit.Before

import static org.mockito.Mockito.*

@TestMixin(GrailsUnitTestMixin)
class SinglePageComponentValidateUrlTest {

    private SinglePageComponent target

    private PageComponent pageComponent
    private GrailsMock mockControlContainer
    private GrailsMock mockControlPresenter
    private Image validUrlImage
    private Image propertiesValidImage
    private Image invalidImage
    private Property property
    private Property.ValueChangeEvent event

    @Before
    public void setUp() throws Exception {
        pageComponent = new PageComponent(url: "testUrl", substitutionVariables: [var1: "value1", var2: "value2"])
        mockControlPresenter = mockFor(KikuyuPresenter)
        mockControlContainer = mockFor(EditPageView)

        Properties properties = new Properties()
        properties.setProperty("real", "realvalue")

        mockControlPresenter.demand.getUrlSymbolResolver(1..2) {
            new UrlSymbolResolver(urlSymbolProperties: properties)
        }

        target = new SinglePageComponent(pageComponent, mockControlPresenter.createMock(), mockControlContainer.createMock())

        validUrlImage = mock(Image)
        propertiesValidImage = mock(Image)
        invalidImage = mock(Image)
        event = mock(Property.ValueChangeEvent)
        property = mock(Property)
        when(event.property).thenReturn(property)
    }

    public void testEmptyField() throws Exception {

        when(property.value).thenReturn("")

        target.validateUrl(event, validUrlImage, propertiesValidImage, invalidImage)

        verify(validUrlImage).visible = false
        verify(propertiesValidImage).visible = false
        verify(invalidImage).visible = false
        verifyNoMoreInteractions(validUrlImage, propertiesValidImage, invalidImage)
    }

    public void testHttpField() throws Exception {

        when(property.value).thenReturn("http://s.s?s=s")

        target.validateUrl(event, validUrlImage, propertiesValidImage, invalidImage)

        verify(validUrlImage).visible = false
        verify(propertiesValidImage).visible = false
        verify(invalidImage).visible = false

        verify(validUrlImage).visible = true

        verifyNoMoreInteractions(validUrlImage, propertiesValidImage, invalidImage)
    }

    public void testInvalidHttpField() throws Exception {

        when(property.value).thenReturn("http://s<")

        target.validateUrl(event, validUrlImage, propertiesValidImage, invalidImage)

        verify(validUrlImage).visible = false
        verify(propertiesValidImage).visible = false
        verify(invalidImage).visible = false

        verify(invalidImage).visible = true

        verifyNoMoreInteractions(validUrlImage, propertiesValidImage, invalidImage)
    }

    public void testValidSymbolField() throws Exception {

        when(property.value).thenReturn("real/path")

        target.validateUrl(event, validUrlImage, propertiesValidImage, invalidImage)

        verify(validUrlImage).visible = false
        verify(propertiesValidImage).visible = false
        verify(invalidImage).visible = false

        verify(propertiesValidImage).visible = true

        verifyNoMoreInteractions(validUrlImage, propertiesValidImage, invalidImage)
    }

    public void testValidSymbolField2() throws Exception {

        when(property.value).thenReturn("real/path/more")

        target.validateUrl(event, validUrlImage, propertiesValidImage, invalidImage)

        verify(validUrlImage).visible = false
        verify(propertiesValidImage).visible = false
        verify(invalidImage).visible = false

        verify(propertiesValidImage).visible = true

        verifyNoMoreInteractions(validUrlImage, propertiesValidImage, invalidImage)
    }

    public void testInvalidSymbolField() throws Exception {

        when(property.value).thenReturn("test/path")

        target.validateUrl(event, validUrlImage, propertiesValidImage, invalidImage)

        verify(validUrlImage).visible = false
        verify(propertiesValidImage).visible = false
        verify(invalidImage).visible = false

        verify(invalidImage).visible = true

        verifyNoMoreInteractions(validUrlImage, propertiesValidImage, invalidImage)
    }
}
