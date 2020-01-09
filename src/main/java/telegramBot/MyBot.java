package telegramBot;

import models.Race;
import models.User;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import services.RaceService;
import services.UserService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    public MyBot(DefaultBotOptions options) {
        super(options);
    }

    private boolean scenario;
    private boolean date = false;

    UserService userService = new UserService();
    RaceService raceService = new RaceService();

    List<User> userList = userService.findAllUsers();
    List<Race> racesList = raceService.findAllRaces();

    Race getRace(String str){
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

    boolean isName(String name) {
        boolean i = false;
        for (User u:userList) {
            i = u.getUser_name().equals(name);
        }
       return i;
    }

    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {

            if (update.getMessage().getText().equals("/admin")) {
                try {
                    //TODO ПРОСМОТР СОЗДАНЫХ ГОНОК, ПРОСМОТР УЧАСТНИКОВ НА КАЖДОЙ ГОНКЕ
                    execute(sendAdminInlineKeyBoardMessage(update.getMessage().getChatId())
                            .setText("Привет админ!"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (scenario) {
                String date = update.getMessage().getText();

                SimpleDateFormat format = new SimpleDateFormat();
                format.applyPattern("dd/MM/yyyy");
                    try {
                        Date docDate = format.parse(date);
                        System.out.println(docDate);
                        scenario = false;
                        Timestamp ts = new Timestamp(docDate.getTime());
                        if (!scenario) {
                            try {
                                execute(new SendMessage()
                                        .setChatId(update.getMessage().getChatId())
                                        .setText("Вы выбрали дату " + date));

                                Race race = new Race(ts);
                                RaceService raceService = new RaceService();
                                raceService.saveRace(race);

                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (ParseException e) {
                        try {
                            execute(new SendMessage()
                                    .setChatId(update.getMessage().getChatId())
                                    .setText("Введите дату в формате: DD/MM/YYYY"));
                        } catch (TelegramApiException ex) {
                            e.printStackTrace();
                        }
                }
            }
            else {
                try {
                    execute(sendMainInlineKeyBoardMessage(update.getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("reg"))
                try {
                    String userName = update.getCallbackQuery().getMessage().getChat().getUserName();

                    if (!isName(userName)){
                        User user = new User(userName);
                        userService.saveUser(user);
                        execute(sendREGInlineKeyBoardMessage(update.getCallbackQuery().getMessage().getChatId()));
                    } else {
                        try {
                            execute(new SendMessage()
                                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText("Вы уже зарегестрированы"));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            else if (update.getCallbackQuery().getData().equals("race")) {
                try {

                    //TODO ПОЗДРАВЛЯЕМ, ВЫ ЗАРЕГЕСТРИРОВАНЫ НА ГОНКУ ТАКОГО-ТО ЧИСЛА

                    execute(sendChooseRaceInlineKeyBoardMessage(update.getCallbackQuery().getMessage().getChatId()));
                    date = true;

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getCallbackQuery().getData().equals("status")) {
                try {

                    //TODO ВЫБОР ГОНКИ
                    //TODO ВЫ ЗАРЕГЕСТРИРОВАНЫ НА ГОНКИ


                    execute(new SendMessage().setText(
                            update.getCallbackQuery().getData())
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getCallbackQuery().getData().equals("create")) {
                try {
                    execute(new SendMessage()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Введите дату в формате: DD/MM/YYYY"));
                    scenario = true;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
            else if (date) {
                String date = update.getCallbackQuery().getData();
                String userName = update.getCallbackQuery().getMessage().getChat().getUserName();

                for (User u:userList) {

                    if(u.getUser_name().equals(userName)){
                            //TODO
                         u.addRaceToUser(getRace(date));
                        System.out.println(date);
                    };
                }

//                try {
//                    //TODO@!!!!
//                    execute(new SendMessage()
//                            .setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("//TODO"));
//                    //TODO@!!!!
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    private static InlineKeyboardButton raceButton() {
        final String RACE = "race";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Хочу гонять!")
                .setCallbackData(RACE)
                .build();
        return button;
    }

    private static InlineKeyboardButton regButton() {
        final String REG = "reg";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Зарегестрироваться")
                .setCallbackData(REG)
                .build();
        return button;
    }

    private static InlineKeyboardButton statusButton() {
        final String STATUS = "status";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Мой статус")
                .setCallbackData(STATUS)
                .build();
        return button;
    }

    private static InlineKeyboardButton createRace() {
        final String CREATE = "create";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Создать событие")
                .setCallbackData(CREATE)
                .build();
        return button;
    }

    private static List<InlineKeyboardButton>  chooseRace() {
        RaceService raceService = new RaceService();
        List<Race> raceList = raceService.findAllRaces();

        InlineKeyboardButton button = null;
        List <InlineKeyboardButton> list = new ArrayList<>();
        for (Race r:raceList) {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("dd/MM/yyyy");
            String date = format.format(r.getDate_of_race().getTime());
             button = new InlineKeyboardButtonBuilder()
                    .setText(date)
                    .setCallbackData(date)
                    .build();

             list.add(button);
        }
        return list;
    }


    public SendMessage sendMainInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(raceButton(), regButton(), statusButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setText("Привет, выбери один из пунктов меню!")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage sendREGInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(raceButton(), statusButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setText("Поздравляем! Теперь можешь выбрать удобное время для гонки и получить инфо о своем статусе")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage sendChooseRaceInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(chooseRace());
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setText("Выбери одну из возможных дат: ")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage sendAdminInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(createRace()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public String getBotUsername() {
        return "tets88_bot";
    }

    public String getBotToken() {
        return "1036923097:AAEK_tX1pC6vvbMOm9wsC-6QWo3v9oHFA7c";
    }
}