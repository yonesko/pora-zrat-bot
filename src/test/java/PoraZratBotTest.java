import java.util.LinkedList;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import util.ReflectionUtils;

/**
 * @author Gleb Danichev
 */
public class PoraZratBotTest {

    private final DummyPoraZratBot bot = new DummyPoraZratBot();

    @Test
    public void testEcho() {
        Assert.assertEquals("echo", sendToBot("gleb", 19, "Привет").getText());
    }

    private SendMessage sendToBot(String userName, long chatId, String text) {
        bot.onUpdateReceived(buildUpdate(buildMessage(text, buildChat(chatId), buildUser(userName))));
        return bot.messageResponses.peek();
    }

    private Update buildUpdate(Message message) {
        Update update = new Update();
        FieldSetter.setField(update, ReflectionUtils.getDeclaredField(Update.class, "message"), message);
        return update;
    }

    private Message buildMessage(String text, Chat chat, User user) {
        Message message = new Message();
        FieldSetter.setField(message, ReflectionUtils.getDeclaredField(Message.class, "text"), text);
        FieldSetter.setField(message, ReflectionUtils.getDeclaredField(Message.class, "from"), user);
        FieldSetter.setField(message, ReflectionUtils.getDeclaredField(Message.class, "chat"), chat);
        return message;
    }

    private User buildUser(String userName) {
        User user = new User();
        FieldSetter.setField(user, ReflectionUtils.getDeclaredField(User.class, "userName"), userName);
        return user;
    }

    private Chat buildChat(long chatId) {
        Chat chat = new Chat();
        FieldSetter.setField(chat, ReflectionUtils.getDeclaredField(Chat.class, "id"), chatId);
        return chat;
    }

    private static class DummyPoraZratBot extends PoraZratBot {

        private final Queue<SendMessage> messageResponses = new LinkedList<>();

        DummyPoraZratBot() {
        }

        @Override
        void sendSafely(SendMessage message) {
            messageResponses.add(message);
        }
    }

}
