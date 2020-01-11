import dao.RaceDAOImpl;
import dao.UserDAOImpl;
import models.Race;
import models.User;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import services.RaceService;
import services.UserService;
import telegramBot.MyBot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String PROXY_HOST = "127.0.0.1" /* proxy host */;
    private static Integer PROXY_PORT = 9150 /* proxy port */;

    public static void main(String[] args) {

        try {
            ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi();
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            MyBot bot = new MyBot(botOptions);
            botsApi.registerBot(bot);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        RaceService raceService = new RaceService();
        UserService userService = new UserService();
        UserDAOImpl userDAO = new UserDAOImpl();
        RaceDAOImpl raceDAO = new RaceDAOImpl();
    }

}
