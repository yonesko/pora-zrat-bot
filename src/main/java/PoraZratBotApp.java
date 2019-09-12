import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PoraZratBotApp {

    public static void main(String[] args) throws TelegramApiException {
        ApiContextInitializer.init();
        new TelegramBotsApi().registerBot(new PoraZratBot());
    }
}
