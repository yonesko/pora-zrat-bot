import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

/**
 * @author Gleb Danichev
 */
public class PoraZratBotTest {

    @Test
    public void onUpdateReceivedTest() {
        PoraZratBot bot = mock(PoraZratBot.class);
        doCallRealMethod().when(bot).onUpdateReceived(any());
    }
}
