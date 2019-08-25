import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PoraZratBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

    }

    public String getBotUsername() {
        return "PoraZratBot";
    }

    public String getBotToken() {
        return System.getenv("PoraZratBotToken");
    }
}
