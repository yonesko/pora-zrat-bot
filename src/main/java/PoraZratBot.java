import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.stickers.GetStickerSet;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PoraZratBot extends TelegramLongPollingBot {

    private final static Logger logger = LogManager.getLogger();

    private final long GLEB_CHAT_ID = 247065060;

    private final List<String> карательнаяКулинарияStickerIds;

    private final boolean testRun = false;

    private final long КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID = testRun ? GLEB_CHAT_ID : -295100024;

    private final ScheduledExecutorService scheduledExecutorService;

    PoraZratBot() throws TelegramApiException {
        карательнаяКулинарияStickerIds = execute(new GetStickerSet("kulinar"))
            .getStickers()
            .stream().map(Sticker::getFileId)
            .collect(Collectors.toList());
        logger.info("Loaded sticker ids: " + карательнаяКулинарияStickerIds);
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(
            () -> {
                if (isWeekend()) {
                    logger.info("Don't work today, skip");
                    return;
                }
                try {
                    sendSticker();
                    sendMessage("К обеду, господа!");
                } catch (TelegramApiException e) {
                    logger.error("Can't send message", e);
                }
            },
            toNextLocalTime(LocalTime.of(14, 0)).getSeconds(),
            Duration.ofDays(1).getSeconds(),
            TimeUnit.SECONDS
        );
        scheduledExecutorService.scheduleAtFixedRate(
            () -> {
                if (isWeekend()) {
                    logger.info("Don't work today, skip");
                    return;
                }
                try {
                    sendSticker();
                    sendMessage("К полднЕку, господа!");
                } catch (TelegramApiException e) {
                    logger.error("Can't send message", e);
                }
            },
            toNextLocalTime(LocalTime.of(16, 58)).getSeconds(),
            Duration.ofDays(1).getSeconds(),
            TimeUnit.SECONDS
        );
        if (testRun) {
            scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    try {
                        sendSticker();
                        sendMessage("Тестовый тест");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                },
                toNextLocalTime(LocalTime.now()).getSeconds(),
                5,
                TimeUnit.SECONDS
            );
        }
    }

    private boolean isWeekend() {
        return LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY
               || LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private Duration toNextLocalTime(LocalTime localTime) {
        if (testRun) {
            localTime = LocalTime.now().plusSeconds(10);
        }
        Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().with(localTime));
        return duration.isNegative() ? duration.plusDays(1) : duration;
    }

    public void onUpdateReceived(Update update) {
        logger.info(update);
    }

    private void sendSticker() throws TelegramApiException {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID);
        sendSticker.setSticker(карательнаяКулинарияStickerIds.get(
            new Random().nextInt(карательнаяКулинарияStickerIds.size())
        ));
        this.execute(sendSticker);
        logger.info("Sent" + sendSticker);
    }

    private void sendMessage(String text) throws TelegramApiException {
        String wallet = new Random().nextInt(100) <= 20 ? " https://yasobe.ru/na/glebio" : "";
        SendMessage message = new SendMessage(КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID, text + wallet);
        this.execute(message);
        logger.info("Sent" + message);
    }

    public String getBotUsername() {
        return "PoraZratBot";
    }

    public String getBotToken() {
        return System.getenv("PORA_ZRAT_BOT_TOKEN");
    }
}
