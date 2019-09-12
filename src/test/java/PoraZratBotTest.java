import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

/**
 * @author Gleb Danichev
 */
public class PoraZratBotTest {

    private final PoraZratBot bot;

    public PoraZratBotTest() {
        bot = mock(PoraZratBot.class);
        doCallRealMethod().when(bot).onUpdateReceived(any());
    }

    @Test
    public void onUpdateReceivedTest() {


    }

    private void sendToBot(long chatId, String text) throws NoSuchFieldException {
        bot.onUpdateReceived(buildUpdate(buildMessage(text, buildChat(chatId))));
    }

    private Update buildUpdate(Message message) throws NoSuchFieldException {
        Update update = new Update();
        FieldSetter.setField(update, Update.class.getDeclaredField("message"), message);
        return update;
    }

    private Message buildMessage(String text, Chat chat) throws NoSuchFieldException {
        Message message = new Message();
        FieldSetter.setField(message, Message.class.getDeclaredField("text"), text);
        FieldSetter.setField(message, Message.class.getDeclaredField("chat"), chat);
        return message;
    }

    private Chat buildChat(long chatId) throws NoSuchFieldException {
        Chat chat = new Chat();
        FieldSetter.setField(chat, Chat.class.getDeclaredField("id"), chatId);
        return chat;
    }
}
