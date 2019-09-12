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
        Assert.assertEquals("echo", sendToBot("gleb", 19, "/start").getText());
    }

    @Test
    public void testHurryUp() {
        SendMessage reaction = sendToBot("ivan", 22, "/i_want_zrat_now");
        Assert.assertTrue(reaction.getText(), reaction.getText().startsWith("@ivan хочет есть сейчас!"));
        Assert.assertEquals(PoraZratBot.КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID + "", reaction.getChatId());
    }

    @Test
    public void testSpecialChannel() {
        SendMessage reaction = sendToBot("ivan", 224, "отошли в клуб как дела, что едите?");
        Assert.assertEquals("ivan has no right for special channel", "echo", reaction.getText());
        reaction = sendToBot("glebone", 22, "отошли в клуб как дела, что едите?");
        Assert.assertEquals("как дела, что едите?", reaction.getText());
        Assert.assertEquals(PoraZratBot.КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID + "", reaction.getChatId());

    }

    private SendMessage sendToBot(String userName, long chatId, String text) {
        bot.onUpdateReceived(buildUpdate(buildMessage(text, buildChat(chatId), buildUser(userName))));
        return bot.lastMessageResponse;
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

        private SendMessage lastMessageResponse;

        DummyPoraZratBot() {
        }

        @Override
        void sendSafely(SendMessage message) {
            lastMessageResponse = message;
        }
    }

}
