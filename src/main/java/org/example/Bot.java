package org.example;


import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private List<User>users;

    private Picture picture;

    public Bot(){
        this.users=new ArrayList<>();
        this.picture = new Picture();
    }
    @Override
    public String getBotToken() {
        return "6045865002:AAEstVaGcS2e0Bp3UXU0HDw4xLyREt_FayM";
    }
    @Override
    public String getBotUsername() {
        return "Hila28Bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = getChatID(update);
        User user = isUserExist(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (user==null){
            User user1 = new User(chatId);
            users.add(user1);
        }

        if (user.getStatus()==Constants.GET_WIDTH_LEVEL) {
            sendMessage.setText("What are the dimensions of the desired image:\n" +
                    "1.What width do you want?");
            user.setStatus(Constants.WAIT_TO_WIDTH);

        }else if (user.getStatus()==Constants.WAIT_TO_WIDTH || user.getStatus()==Constants.WAIT_TO_HEIGHT){
            boolean isValid = isNumeric(update.getMessage().getText());//ככה לוקחים מה שהוא כתב
            if (isValid){
                if (user.getStatus()==Constants.WAIT_TO_WIDTH){
                    user.addParameter(update.getMessage().getText());
                    sendMessage.setText("What height do you want?");
                    user.setStatus(Constants.WAIT_TO_HEIGHT);

                }else if (user.getStatus()==Constants.WAIT_TO_HEIGHT){
                    user.addParameter(update.getMessage().getText());
                    sendMessage.setReplyMarkup(getKeyboard(Constants.SHAPE,Constants.SHAPE_CODE));
                    sendMessage.setText("What template do you want?");
                    user.setStatus(Constants.SHAPE_LEVEL);
                }
            }else {
                sendMessage.setText("only numbers!");
            }

        }else if (user.getStatus()==Constants.SHAPE_LEVEL){

            sendMessage.setReplyMarkup(getKeyboard(Constants.COLOR, Constants.COLOR_CODE));
            sendMessage.setText("Choose the first color for the image:");
            user.addParameter(update.getCallbackQuery().getData());// לקחת את הקוד של הכפתור שהמשתמש לחץ
            user.setStatus(Constants.FIRST_COLOR_LEVEL);

        } else if (user.getStatus()==Constants.FIRST_COLOR_LEVEL) {
            sendMessage.setReplyMarkup(getKeyboard(Constants.COLOR, Constants.COLOR_CODE));
            sendMessage.setText("Choose the second color for the image:");
            user.addParameter(update.getCallbackQuery().getData());// לקחת את הקוד של הכפתור שהמשתמש לחץ

            user.setStatus(Constants.SECOND_COLOR_LEVEL);
        } else if (user.getStatus()==Constants.SECOND_COLOR_LEVEL) {

            user.addParameter(update.getCallbackQuery().getData());
            this.picture.createPictureByParameter(user.getParameters());
            sendMessage.setText("Image completed successfully :)");
            user.setStatus(Constants.GET_WIDTH_LEVEL);
        }
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
        //לאחר מכן, הבוט ייצור את התמונה וישמור אותה על גבי הדיסק
        }
    private boolean isNumeric(String str) {
        boolean result =true;
        if (str == null || str.length() == 0) {
            result= false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                result= false;
            }
        }
        return result;
    }
    private long getChatID(Update update){//איך לקחת ID של המשתמש בשביל להחזיר לו תשובה
        long result=0;
        if (!update.hasCallbackQuery()){
            result=update.getMessage().getChatId();
        }else {
            result=update.getCallbackQuery().getMessage().getChatId();
        }
        return result;
    }
    private User isUserExist(long chatId){
        User result = null;
        for (User user:this.users){
            if (user.isEqualChatId(chatId)){
                result = user;
                break;
            }
        }
        return result;
    }
    private InlineKeyboardMarkup getKeyboard(String [] options ,String [] optionsCode ){
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (int i =0; i<options.length; i++){
            InlineKeyboardButton newButton = new InlineKeyboardButton(options[i]);
            newButton.setCallbackData(optionsCode[i]);
            buttons.add(newButton);
        }
        List<List<InlineKeyboardButton>> keyboard = Arrays.asList(buttons);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
}











