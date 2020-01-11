package telegramBot;

import lombok.SneakyThrows;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    public MyBot(DefaultBotOptions options) {
        super(options);
    }

    UserService userService = new UserService();
    RaceService raceService = new RaceService();

    private String alreadyRegistered = "Вы уже зарегестрированы";
    private String notRegistered = "Вы не зарегестрированы";
    private String setDate = "введите дату в формате dd/MM/yyyy";
    private String registered = " вы зарегестрированы на гонку: ";
    private String alreadyRegisteredDate = " вы уже зарегестрированы на эту дату";


    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "/admin":
                    sendAdminKeyBoard(update);
                    break;
                default:
                    sendMainKeyboard(update);
            }
        } else if (update.hasCallbackQuery()) {

            switch (update.getCallbackQuery().getData()) {
                case "reg":
                    if (userService.getEntityByString(getUserName(update)) == null) {
                        userService.saveUser(new User(getUserName(update)));
                        sendRegistrationKeyboard(update, getUserName(update));
                    } else {
                        sendResponse(update, alreadyRegistered);
                    }
                    break;

                case "race":
                    if (userService.getEntityByString(getUserName(update)) == null) {
                        sendResponse(update, notRegistered);
                    } else {
                        sendChooseRaceKeyboard(update);
                    }
                    break;

                case "status":
                    if (userService.getEntityByString(getUserName(update)) == null) {
                        sendResponse(update, notRegistered);
                    } else {
                        List<Race> userRaces = userService.getEntityByString(getUserName(update)).getUserRaces();
                        sendResponse(update,registered,getUserName(update),userRaces);
                    }
                    break;

                case "create":
                        sendResponse(update,setDate);
                    break;

                default:
                    dateChecker(update.getCallbackQuery().getData(),
                            getUserName(update),
                            update);
                    break;
            }
        }
    }

    String getUserName(Update update){
        return update.getCallbackQuery().getMessage().getChat().getUserName();
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
                sendResponse(update,alreadyRegisteredDate,userName);
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
    public SendMessage adminInlineKeyBoardMessage(long chatId) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(createRace()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId)
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    @SneakyThrows
    private void sendAdminKeyBoard(Update update) {
        //TODO ПРОСМОТР СОЗДАНЫХ ГОНОК, ПРОСМОТР УЧАСТНИКОВ НА КАЖДОЙ ГОНКЕ
        execute(adminInlineKeyBoardMessage(update.getMessage().getChatId()).setText("Привет админ!"));
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
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(setText));

    }
    @SneakyThrows
    private void sendResponse(Update update, String setText, String userName) {
        execute(new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(userName + setText));

    }
    @SneakyThrows
    private void sendResponse(Update update, String setText, String userName, List<Race> userRaces) {
        execute(new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(userName + setText + userRaces.toString()));

    }

    @Override
    public String getBotUsername() {
        return "tets88_bot";
    }
    @Override
    public String getBotToken() {
        return "1036923097:AAEK_tX1pC6vvbMOm9wsC-6QWo3v9oHFA7c";
    }

}


