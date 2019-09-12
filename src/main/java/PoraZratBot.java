import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.stickers.GetStickerSet;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PoraZratBot extends TelegramLongPollingBot {

    private final static Logger logger = LogManager.getLogger();

    private static final String ОТОШЛИ_В_КЛУБ = "отошли в клуб ";

    private final long GLEB_CHAT_ID = 247065060;

    private final List<String> карательнаяКулинарияStickerIds;

    private final boolean testRun = false;

    private final long КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID = testRun ? GLEB_CHAT_ID : -295100024;

    PoraZratBot() throws TelegramApiException {
        карательнаяКулинарияStickerIds = execute(new GetStickerSet("kulinar"))
            .getStickers()
            .stream().map(Sticker::getFileId)
            .collect(Collectors.toList());
        logger.info("Loaded sticker ids: " + карательнаяКулинарияStickerIds);
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(
            () -> {
                if (isWeekend()) {
                    logger.info("Don't work today, skip");
                    return;
                }
                sendStickerToClub();
                sendMessageToClub("К обеду, господа!");
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
                sendStickerToClub();
                sendMessageToClub("ПолднЕков нет, живите с этим, господа!");
            },
            toNextLocalTime(LocalTime.of(16, 58)).getSeconds(),
            Duration.ofDays(1).getSeconds(),
            TimeUnit.SECONDS
        );
        if (testRun) {
            scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    sendStickerToClub();
                    sendMessageToClub("Тестовый тест");
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
        final String text = update.getMessage().getText();
        String userName = update.getMessage().getFrom().getUserName();
        if (text.equals("/i_want_zrat_now") || text.equals("/i_want_zrat_now@pora_zrat_bot")) {
            sendMessageToClub("@" + userName + " хочет есть сейчас!");
        } else if (userName.equals("glebone") && text.startsWith(ОТОШЛИ_В_КЛУБ)) {
            sendSafely(new SendMessage(
                КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID,
                text.replaceFirst(ОТОШЛИ_В_КЛУБ, "")
            ));
        } else {
            sendSafely(new SendMessage(update.getMessage().getChatId(), "echo"));
        }
    }

    private void sendSafely(PartialBotApiMethod message) {
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            logger.error(e);
            return;
        }
        logger.info("Sent" + message);
    }

    private void sendStickerToClub() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID);
        sendSticker.setSticker(карательнаяКулинарияStickerIds.get(
            new Random().nextInt(карательнаяКулинарияStickerIds.size())
        ));
        sendSafely(sendSticker);
    }

    private void sendMessageToClub(String text) {
        try {
            this.execute(new SendMessage(КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID, text + salt()));
        } catch (TelegramApiException e) {
            logger.error("Can't sendMessage", e);
            return;
        }
        logger.info("Sent" + new SendMessage(КЛУБ_ЛЮБИТЕЛЕЙ_ПОЕСТЬ_CHAT_ID, text + salt()));
    }

    private String salt() {
        String salt = new Random().nextInt(100) <= 20 ? " https://yasobe.ru/na/glebio" : "";
        if (LocalDate.now().equals(LocalDate.of(2019, Month.SEPTEMBER, 17))) {
            salt = " Чо как там без меня, сырки принесли?";
        }
        return salt;
    }

    public String getBotUsername() {
        return "PoraZratBot";
    }

    public String getBotToken() {
        return System.getenv("PORA_ZRAT_BOT_TOKEN");
    }
}
