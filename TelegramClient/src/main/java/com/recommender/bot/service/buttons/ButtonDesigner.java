package com.recommender.bot.service.buttons;

import com.recommender.bot.entities.Callback;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ButtonDesigner {
    public InlineKeyboardMarkup getPaginationKeyboard(String state,
                                                      String link,
                                                      Integer currentPage,
                                                      Integer maxPageNumber) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(designActionButtons(state, currentPage))
                .keyboardRow(designPaginationButtons(state, currentPage, maxPageNumber))
                .keyboardRow(designLinkButton(link))
                .build();
    }

    public InlineKeyboardMarkup getPaginationKeyboardAfterRateAction(String state,
                                                                     String link,
                                                                     Integer currentPage,
                                                                     Integer maxPageNumber,
                                                                     Double rating) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(designActionButtonsAfterRateAction(state, currentPage, rating))
                .keyboardRow(designPaginationButtons(state, currentPage, maxPageNumber))
                .keyboardRow(designLinkButton(link))
                .build();
    }

    public InlineKeyboardMarkup getKeyboard(String state,
                                            String link,
                                            Integer currentPage) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(designActionButtons(state, currentPage))
                .keyboardRow(designLinkButton(link))
                .build();
    }

    public InlineKeyboardMarkup getKeyboardAfterRateAction(String state,
                                                           String link,
                                                           Integer currentPage,
                                                           Double rating) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(designActionButtonsAfterRateAction(state, currentPage, rating))
                .keyboardRow(designLinkButton(link))
                .build();
    }

    public InlineKeyboardMarkup getScaleButtons(String state,
                                                String link,
                                                Integer currentPage) {
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());
        for (double i = 0.5, j = i + 2.5; i <= 2.5; i += 0.5, j += 0.5) {
            result.get(0).add(InlineKeyboardButton.builder()
                    .text(String.valueOf(i))
                    .callbackData(Callback.ACTION.RATE + "|" + state + "|" + i + "|" + currentPage)
                    .build()
            );
            result.get(1).add(InlineKeyboardButton.builder()
                    .text(String.valueOf(j))
                    .callbackData(Callback.ACTION.RATE + "|" + state + "|" + j + "|" + currentPage)
                    .build()
            );
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(result.get(0))
                .keyboardRow(result.get(1))
                .keyboardRow(designLinkButton(link))
                .build();
    }

    private List<InlineKeyboardButton> designLinkButton(String link) {
        var button = InlineKeyboardButton.builder()
                .text("Подробнее о фильме")
                .url(link)
                .build();

        return List.of(button);
    }

    private List<InlineKeyboardButton> designActionButtons(String state,
                                                           Integer currentPage) {
        var rateButton = InlineKeyboardButton.builder()
                .text("Оценить")
                .callbackData(Callback.ACTION.GET_SCALE + "|" + state + "|-|" + currentPage)
                .build();
        var addButton = InlineKeyboardButton.builder()
                .text("Отложить")
                .callbackData(Callback.ACTION.SAVE + "|" + state + "|-|" + currentPage)
                .build();

        return List.of(rateButton, addButton);
    }

    private List<InlineKeyboardButton> designActionButtonsAfterRateAction(String state,
                                                                          Integer currentPage,
                                                                          Double rating) {
        var rateButton = new InlineKeyboardButton();
        if (rating == -1)
            rateButton.setText("Оценить");
        else
            rateButton.setText("Ваша оценка: " + rating);
        rateButton.setCallbackData(Callback.ACTION.GET_SCALE + "|" + state + "|-|" + currentPage);

        var addButton = InlineKeyboardButton.builder()
                .text("Удалить")
                .callbackData(Callback.ACTION.DELETE + "|" + state + "|-|" + currentPage)
                .build();

        return List.of(rateButton, addButton);
    }

    private List<InlineKeyboardButton> designPaginationButtons(String state,
                                                               Integer currentPage,
                                                               Integer maxPageNumber) {
        var leftArrowButton = InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(Callback.ACTION.PAGE_TURN + "|" + state + "|l|" + currentPage)
                .build();
        var rightArrowButton = InlineKeyboardButton.builder()
                .text("➡️")
                .callbackData(Callback.ACTION.PAGE_TURN + "|" + state + "|r|" + currentPage)
                .build();
        var counterButton = InlineKeyboardButton.builder()
                .text(currentPage + "/" + maxPageNumber)
                .callbackData(Callback.ACTION.IGNORE)
                .build();

        return List.of(leftArrowButton, counterButton, rightArrowButton);
    }
}