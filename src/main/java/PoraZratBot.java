import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.stickers.GetStickerSet;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PoraZratBot extends TelegramLongPollingBot {

    private final long КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID = -295100024;

    private final long GLEB_CHAT_ID = 247065060;

    private final List<String> карательнаяКулинарияStickerIds;

    PoraZratBot() throws TelegramApiException {
        карательнаяКулинарияStickerIds = execute(new GetStickerSet("kulinar"))
            .getStickers()
            .stream().map(Sticker::getFileId)
            .collect(Collectors.toList());

        Thread thread = new Thread(this::run);
        thread.setName("sticker sender");
        thread.setDaemon(true);
        thread.start();
    }

    public void onUpdateReceived(Update update) {
        System.out.println(update);
    }

    private void run() {
        try {
            SendSticker sendSticker = new SendSticker();
            sendSticker.setChatId(GLEB_CHAT_ID);
            sendSticker.setSticker(карательнаяКулинарияStickerIds.get(
                new Random().nextInt(карательнаяКулинарияStickerIds.size())
            ));
            this.execute(sendSticker);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "PoraZratBot";
    }

    public String getBotToken() {
        return "944944764:AAHG45YhorVcwx530UB0FtmjepK4ChT7q9I";
    }
}
