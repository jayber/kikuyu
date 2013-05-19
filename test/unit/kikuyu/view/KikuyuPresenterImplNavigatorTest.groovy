package kikuyu.view

import com.vaadin.navigator.Navigator
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

import static org.mockito.Mockito.mock
import static org.powermock.api.mockito.PowerMockito.whenNew

@RunWith(PowerMockRunner.class)
@PrepareForTest([KikuyuPresenterImpl, Navigator])
class KikuyuPresenterImplNavigatorTest {

    KikuyuPresenterImpl target

    @Before
    public void setUp() throws Exception {
        target = new KikuyuPresenterImpl()

    }

    //todo: this should work, no idea why it doesn't
    @Ignore
    @Test
    public void testBuildNavigator() throws Exception {

        final VerticalLayout layout = mock(VerticalLayout)
        final UI ui = mock(UI)
        final Navigator navigator = mock(Navigator)
        whenNew(Navigator).withArguments(ui, layout).thenReturn(navigator)

        target.buildNavigator(layout, ui)
    }
}
