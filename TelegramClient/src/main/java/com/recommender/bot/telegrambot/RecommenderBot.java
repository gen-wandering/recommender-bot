package com.recommender.bot.telegrambot;

import com.recommender.bot.entities.Callback;
import com.recommender.bot.entities.Commands;
import com.recommender.bot.service.messages.MessageAssembler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.recommender.bot.entities.Callback.action;

/*
 * Информация в CallbackData может быть распределена по всем кнопкам, так как
 * во время получения информации доступен объект Message!
 * */

@Component
public class RecommenderBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final MessageAssembler messageAssembler;

    public RecommenderBot(@Value("${bot.token}") String botToken,
                          @Value("${bot.username}") String botUsername,
                          MessageAssembler messageAssembler) {
        super(botToken);
        this.botUsername = botUsername;
        this.messageAssembler = messageAssembler;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            try {
                handleCallback(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.hasEntities()) {
            MessageEntity messageEntity = message.getEntities().get(0);

            if (messageEntity.getType().equals("bot_command")) {
                switch (messageEntity.getText()) {
                    case Commands.START -> execute(messageAssembler.registerMessage(message));
                    case Commands.VIEWS -> execute(messageAssembler.topViewsMessage(message));
                    case Commands.RATING -> execute(messageAssembler.topRatingsMessage(message));
                    case Commands.RECOMMEND -> execute(messageAssembler.recommendationMessage(message));
                    case Commands.MINE -> execute(messageAssembler.mineMessage(message));
                    default -> execute(messageAssembler.unsupportedCommand(message));
                }
            }
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) throws TelegramApiException {

        System.out.println(callbackQuery.getData());
        var data = callbackQuery.getData().split("\\|");

        switch (data[action]) {
            case Callback.ACTION.PAGE_TURN -> execute(messageAssembler.editMessageOnPageTurn(callbackQuery));
            case Callback.ACTION.GET_SCALE -> execute(messageAssembler.rateScaleMessage(callbackQuery));
            case Callback.ACTION.RATE -> execute(messageAssembler.rateMovieMessage(callbackQuery));
            case Callback.ACTION.SAVE -> execute(messageAssembler.saveMovieMessage(callbackQuery));
            case Callback.ACTION.DELETE -> execute(messageAssembler.deleteMovieMessage(callbackQuery));
        }
    }
}