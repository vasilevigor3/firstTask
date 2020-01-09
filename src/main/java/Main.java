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
import java.text.SimpleDateFormat;
import java.util.List;

public class Main {
    private static String PROXY_HOST = "127.0.0.1" /* proxy host */;
    private static Integer PROXY_PORT = 9150 /* proxy port */;

   static RaceService raceService = new RaceService();


    static Race getRace(String str){
        Race race = null;
        List<Race> racesList = raceService.findAllRaces();
        for (Race r:racesList) {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("dd/MM/yyyy");
            String date = format.format(r.getDate_of_race().getTime());
            if(date.equals(str)){
                race = r;
            }
        }
        return race;
    }

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

        UserService userService = new UserService();
        RaceService raceService = new RaceService();

//        Race race = new Race(new Date("01/01/12"));
//        raceService.saveRace(race);

//        raceService.saveRace(race);
//        User user2 = new User("user");
//        User user3 = new User("test_u");
//        userService.saveUser(user3);
        User user = userService.findUser(1);
        User user2 = userService.findUser(2);
        User user3 = userService.findUser(4);
        User user4 = userService.findUser(5);

        Race race = raceService.findRace(1);
        race.addUserToTheRace(user);
        race.addUserToTheRace(user2);
        race.addUserToTheRace(user3);

//        user.addRaceToUser(race);
        System.out.println(getRace("01/01/2012"));

        user3.addRaceToUser(getRace("01/01/2012"));

        System.out.println(user3.getUserRaces());
        System.out.println(user4.getUserRaces());
        System.out.println(race.getListOfUsersOfOneRace());
    }

}
