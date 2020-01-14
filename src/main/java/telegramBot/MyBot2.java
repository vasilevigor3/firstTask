package telegramBot;

import dao.BotUserDao;
import lombok.SneakyThrows;
import models.BotUser;
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

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.math.BigInteger.valueOf;

public class MyBot2 extends TelegramLongPollingBot {
    public MyBot2(DefaultBotOptions options) {
        super(options);
    }

    BotUserDao botUserDao = new BotUserDao();
    UserService userService = new UserService();
    RaceService raceService = new RaceService();
    private final String alreadyRegistered = "Вы уже зарегестрированы";
    private final String alreadyRegisteredDate = " вы уже зарегестрированы на эту дату";
    private final String notRegistered = "Вы не зарегестрированы";
    private final String registeredOnDate = " вы зарегестрированы на гонку: ";
    private final String setDate = "введите дату в формате dd/MM/yyyy";
    private final String admin = "Что бы создать событие, введите дату в формате дд/мм/гггг";
    private final String state1 = "state1";
    private final String state2 = "state2";
    private final String botName = "tets88_bot";
    private final String botToken = "1036923097:AAEK_tX1pC6vvbMOm9wsC-6QWo3v9oHFA7c";

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            isBotUserExist(update);
            BotUser botUser = botUserDao.getUserByUserName(getUserName(update));
            if (botUser.getUserState().equals(state2) || update.getMessage().getText().equals("/admin")) {

                String date = update.getMessage().getText();

                if (isThisDateValid(date, "dd/MM/yyyy", update)) {
                    botUser.setUserState("state1");
                    botUserDao.update(botUser);
                    raceService.saveRace(new Race(stringToDate(date)));
                    sendResponse(update, "вы создали событие на: " + date);
                } else {
                    stateSetter(update, botUser, state2);
                    botUserDao.update(botUser);
                    System.out.println(botUser);
                }
            }
            switch (botUserDao.getUserByUserName(getUserName(update)).getUserState()) {
                case "state1":
                    sendMainKeyboard(update);
                    break;
                case "state2":
                    sendResponse(update, admin);
                    break;
            }

        } else if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case "reg":
                    if (userService.getEntityByString(getUserNameCallback(update)) == null) {
                        userService.saveUser(new User(getUserNameCallback(update)));
                        sendRegistrationKeyboard(update, getUserNameCallback(update));
                    } else {
                        sendResponseCallBack(update, alreadyRegistered);
                    }
                    break;
                case "race":
                    if (userService.getEntityByString(getUserNameCallback(update)) == null) {
                        sendResponseCallBack(update, notRegistered);
                    } else {
                        sendChooseRaceKeyboard(update);
                    }
                    break;
                case "status":
                    if (userService.getEntityByString(getUserNameCallback(update)) == null) {
                        sendResponseCallBack(update, notRegistered);
                    } else {
                        List<Race> userRaces = userService.getEntityByString(getUserNameCallback(update)).getUserRaces();
                        sendResponseCallBack(update, registeredOnDate, getUserNameCallback(update), userRaces);
                    }
                    break;
                case "create":
                    sendResponseCallBack(update, setDate);
                    break;

                default:
                    dateChecker(update.getCallbackQuery().getData(),
                            getUserNameCallback(update),
                            update);
                    break;
            }
        }
    }


    private boolean isThisDateValid(String dateToValidate, String dateFromat, Update update) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    private LocalDate stringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }
    private void isBotUserExist(Update update) {
        if (botUserDao.findById(getUserId(update)) == null) {
            System.out.println("mark");
            botUserDao.save(new BotUser(getUserName(update), getUserId(update), state1));
        }
    }
    private void stateSetter(Update update, BotUser user, String state) {
        user.setUserState(state);
        botUserDao.update(user);
    }

    public void dateChecker(String data, String userName, Update update) {
        List<Race> races = raceService.findAllRaces();
        User user = userService.getEntityByString(userName);
        List<Race> userRaces = user.getUserRaces();
        for (Race r : races) {
            if (!userRaces.contains(r)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                String sf = formatter.format(r.getDateOfRace());
                if (sf.equals(data)) {
                    user.addRaceToUser(r);
                    userService.updateUser(user);
                    try {
                        execute(new SendMessage()
                                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                                .setText(userName + " вы зарегестрированы на гонку: " + data));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                sendResponseCallBack(update, alreadyRegisteredDate, userName);
            }
        }
    }

    private InlineKeyboardButton raceButton() {
        final String RACE = "race";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Хочу гонять!")
                .setCallbackData(RACE)
                .build();
        return button;
    }
    private InlineKeyboardButton regButton() {
        final String REG = "reg";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Зарегестрироваться")
                .setCallbackData(REG)
                .build();
        return button;
    }
    private InlineKeyboardButton statusButton() {
        final String STATUS = "status";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Мой статус")
                .setCallbackData(STATUS)
                .build();
        return button;
    }
    private InlineKeyboardButton createRace() {
        final String CREATE = "create";
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Создать событие")
                .setCallbackData(CREATE)
                .build();
        return button;
    }
    private List<InlineKeyboardButton> chooseRace() {

        InlineKeyboardButton button;
        List<InlineKeyboardButton> list = new ArrayList<>();
        for (Race r : raceService.findAllRaces()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            String date = formatter.format(r.getDateOfRace());
            button = new InlineKeyboardButtonBuilder()
                    .setText(date)
                    .setCallbackData(date)
                    .build();
            list.add(button);
        }
        return list;
    }

    public SendMessage mainInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(raceButton(), regButton(), statusButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setText("Привет, выбери один из пунктов меню!")
                .setReplyMarkup(inlineKeyboardMarkup);
    }
    public SendMessage regInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(raceButton(), statusButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setText("теперь можешь выбрать удобное время для гонки и получить инфо о своем статусе")
                .setReplyMarkup(inlineKeyboardMarkup);
    }
    public SendMessage chooseRaceInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(chooseRace());
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setText("Выбери одну из возможных дат: ")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    BigInteger getUserId(Update update) {
        return valueOf(update.getMessage().getChat().getId());
    }
    BigInteger getUserIdCallback(Update update) {
        return valueOf(update.getCallbackQuery().getMessage().getChat().getId());
    }
    String getUserName(Update update) {
        return update.getMessage().getChat().getUserName();
    }
    String getUserNameCallback(Update update) {
        return update.getCallbackQuery().getMessage().getChat().getUserName();
    }


    @SneakyThrows
    private void sendMainKeyboard(Update update) {
        execute(mainInlineKeyBoardMessage(update.getMessage().getChatId()));
    }
    @SneakyThrows
    private void sendRegistrationKeyboard(Update update, String userName) {
        execute(new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(userName + ", поздравляем ты зарегестрирован!"));
        execute(regInlineKeyBoardMessage(update.getCallbackQuery().getMessage().getChatId()));
    }
    @SneakyThrows
    private void sendChooseRaceKeyboard(Update update) {
        execute(chooseRaceInlineKeyBoardMessage(update.getCallbackQuery().getMessage().getChatId()));
    }
    @SneakyThrows
    private void sendResponse(Update update, String setText) {
        execute(new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(setText));
    }
    @SneakyThrows
    private void sendResponseCallBack(Update update, String setText) {
        execute(new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(setText));

    }
    @SneakyThrows
    private void sendResponseCallBack(Update update, String setText, String userName) {
        execute(new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(userName + setText));

    }
    @SneakyThrows
    private void sendResponseCallBack(Update update, String setText, String userName, List<Race> userRaces) {
        execute(new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(userName + setText + userRaces.toString()));

    }


    @Override
    public String getBotUsername() {
        return botName;
    }
    @Override
    public String getBotToken() {
        return botToken;
    }
}
