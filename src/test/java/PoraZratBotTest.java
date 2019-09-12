import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Gleb Danichev
 */
public class PoraZratBotTest {

    private final DummyPoraZratBot bot = new DummyPoraZratBot();

    @Test
    public void onUpdateReceivedTest() {
        sendToBot(19, "Привет");

    }

    private BotApiMethod sendToBot(long chatId, String text) {
        bot.onUpdateReceived(buildUpdate(buildMessage(text, buildChat(chatId))));
        return bot.responses.peek();
    }

    private Update buildUpdate(Message message) {
        Update update = new Update();
        FieldSetter.setField(update, ReflectionUtils.getDeclaredField(Update.class, "message"), message);
        return update;
    }

    private Message buildMessage(String text, Chat chat) {
        Message message = new Message();
        FieldSetter.setField(message, ReflectionUtils.getDeclaredField(Message.class, "text"), text);
        FieldSetter.setField(message, ReflectionUtils.getDeclaredField(Message.class, "chat"), chat);
        return message;
    }

    private Chat buildChat(long chatId) {
        Chat chat = new Chat();
        FieldSetter.setField(chat, ReflectionUtils.getDeclaredField(Chat.class, "id"), chatId);
        return chat;
    }

    private static class DummyPoraZratBot extends PoraZratBot {

        private final Queue<BotApiMethod> responses = new LinkedList<>();

        DummyPoraZratBot() {
        }

        @Override
        public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws
            TelegramApiException
        {
            responses.add(method);
            return super.execute(method);
        }

    }

}
