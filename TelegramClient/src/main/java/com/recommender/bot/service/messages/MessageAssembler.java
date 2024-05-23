package com.recommender.bot.service.messages;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.User;
import com.recommender.bot.service.buttons.ButtonDesigner;
import com.recommender.bot.service.data.MovieService;
import com.recommender.bot.service.data.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.recommender.bot.entities.Callback.*;

@Component
public class MessageAssembler {
    private final MovieService movieService;
    private final UserService userService;
    private final ButtonDesigner buttonDesigner;

    public MessageAssembler(MovieService movieService,
                            UserService userService,
                            ButtonDesigner buttonDesigner) {
        this.movieService = movieService;
        this.userService = userService;
        this.buttonDesigner = buttonDesigner;
    }

    public SendMessage registerMessage(Message message) {
        boolean isNewUser = userService.registerUser(message.getFrom().getId());
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());

        String name = message.getFrom().getUserName();
        if (name == null || name.equals("")) {
            name = message.getFrom().getFirstName();
        }
        if (isNewUser)
            sendMessage.setText("Здравствуйте, " + name + "!\n" + "Вы успешно зарегистрированы!");
        else
            sendMessage.setText("Здравствуйте, " + name + "!\n" + "Вы уже зарегистрированы!");
        return sendMessage;
    }

    public SendMessage unsupportedCommand(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Команда не поддерживается")
                .build();
    }

    public SendMessage topViewsMessage(Message message) {
        Movie movie = movieService.getFromViewsTopByPosition(1);

        String topViewsIcon = "\uD83C\uDF1F";
        var entity = MessageEntity.builder().type("text_link").offset(0)
                .length(topViewsIcon.length()).url(movie.getLink()).build();

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(topViewsIcon)
                .replyMarkup(buttonDesigner.getPaginationKeyboard(
                        STATE.VIEWS_TOP,
                        movie.getLink(),
                        1,
                        movieService.getViewsTopSize()))
                .entities(List.of(entity))
                .build();
    }

    public SendMessage topRatingsMessage(Message message) {
        Movie movie = movieService.getFromRatingsTopByPosition(1);

        String topRatingsIcon = "\uD83C\uDFC6";
        var entity = MessageEntity.builder().type("text_link").offset(0)
                .length(topRatingsIcon.length()).url(movie.getLink()).build();

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(topRatingsIcon)
                .replyMarkup(buttonDesigner.getPaginationKeyboard(
                        STATE.RATINGS_TOP,
                        movie.getLink(),
                        1,
                        movieService.getRatingsTopSize()))
                .entities(List.of(entity))
                .build();
    }

    public SendMessage recommendationMessage(Message message) {
        long userId = message.getFrom().getId();
        User user = userService.setMoviesToRecommend(userId);

        String recommendIcon = "\uD83C\uDFAC";
        var entity = MessageEntity.builder()
                .type("text_link")
                .offset(0)
                .length(recommendIcon.length())
                .url(user.getMoviesToRecommend().get(0).getLink())
                .build();

        InlineKeyboardMarkup replyMarkup;
        if (user.getMoviesToRecommend().size() == 1)
            replyMarkup = buttonDesigner.getKeyboard(
                    STATE.RECOMMENDATION,
                    entity.getUrl(),
                    1
            );
        else
            replyMarkup = buttonDesigner.getPaginationKeyboard(
                    STATE.RECOMMENDATION,
                    entity.getUrl(),
                    1,
                    user.getMoviesToRecommend().size()
            );
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(recommendIcon)
                .replyMarkup(replyMarkup)
                .entities(List.of(entity))
                .build();
    }

    public SendMessage mineMessage(Message message) {
        long userId = message.getFrom().getId();
        User user = userService.findUserById(userId);
        var ratedMovies = user.getRatedMovies();

        if (ratedMovies != null && ratedMovies.size() != 0) {
            String myMovieIcon = "\uD83D\uDCBE";
            var entity = MessageEntity.builder()
                    .type("text_link")
                    .offset(0)
                    .length(myMovieIcon.length())
                    .url(ratedMovies.get(0).getMovie().getLink())
                    .build();

            InlineKeyboardMarkup replyMarkup;
            if (ratedMovies.size() == 1)
                replyMarkup = buttonDesigner.getKeyboardAfterRateAction(
                        STATE.MINE,
                        entity.getUrl(),
                        1,
                        ratedMovies.get(0).getRating()
                );
            else
                replyMarkup = buttonDesigner.getPaginationKeyboardAfterRateAction(
                        STATE.MINE,
                        entity.getUrl(),
                        1,
                        ratedMovies.size(),
                        ratedMovies.get(0).getRating()
                );
            return SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(myMovieIcon)
                    .replyMarkup(replyMarkup)
                    .entities(List.of(entity))
                    .build();
        } else {
            var sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Список 'Мои фильмы' пуст");
            return sendMessage;
        }
    }

    public EditMessageText editMessageOnPageTurn(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData().split("\\|");
        var entity = callbackQuery.getMessage().getEntities().get(0);

        int currentPage = Integer.parseInt(data[current_page]);

        if (data[turn_type].equals("l")) currentPage -= 1;
        else currentPage += 1;

        var paginationKeyboard = switch (data[state]) {
            case STATE.VIEWS_TOP -> {
                int topSize = movieService.getViewsTopSize();
                if (currentPage > topSize) currentPage = 1;
                if (currentPage == 0) currentPage = topSize;

                Movie movie = movieService.getFromViewsTopByPosition(currentPage);
                entity.setUrl(movie.getLink());

                yield buttonDesigner.getPaginationKeyboard(
                        data[state], movie.getLink(), currentPage, topSize
                );
            }
            case STATE.RATINGS_TOP -> {
                int topSize = movieService.getRatingsTopSize();
                if (currentPage > topSize) currentPage = 1;
                if (currentPage == 0) currentPage = topSize;

                Movie movie = movieService.getFromRatingsTopByPosition(currentPage);
                entity.setUrl(movie.getLink());

                yield buttonDesigner.getPaginationKeyboard(
                        data[state], movie.getLink(), currentPage, topSize
                );
            }
            case STATE.RECOMMENDATION -> {
                long userId = callbackQuery.getFrom().getId();
                User user = userService.findUserById(userId);

                int size = user.getMoviesToRecommend().size();
                if (currentPage > size) currentPage = 1;
                if (currentPage == 0) currentPage = size;

                Movie movie = user.getMoviesToRecommend().get(currentPage - 1);
                entity.setUrl(movie.getLink());

                if (size == 1)
                    yield buttonDesigner.getKeyboard(
                            data[state], movie.getLink(), currentPage);
                else
                    yield buttonDesigner.getPaginationKeyboard(
                            data[state], movie.getLink(), currentPage, size);
            }
            case STATE.MINE -> {
                long userId = callbackQuery.getFrom().getId();
                User user = userService.findUserById(userId);
                var ratedMovies = user.getRatedMovies();

                int size;
                if (ratedMovies == null || (size = ratedMovies.size()) == 0) yield null;

                if (currentPage > size) currentPage = 1;
                if (currentPage <= 0) currentPage = size;

                Movie movie = ratedMovies.get(currentPage - 1).getMovie();
                entity.setUrl(movie.getLink());

                if (size == 1)
                    yield buttonDesigner.getKeyboardAfterRateAction(
                            data[state], movie.getLink(), currentPage, ratedMovies.get(0).getRating());
                else
                    yield buttonDesigner.getPaginationKeyboardAfterRateAction(
                            data[state], movie.getLink(), currentPage, size, ratedMovies.get(currentPage - 1).getRating());
            }
            default -> throw new IllegalStateException("Unexpected value: " + data[state]);
        };
        var message = callbackQuery.getMessage();

        if (paginationKeyboard == null)
            return EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text("Список 'Мои фильмы' пуст")
                    .build();
        else
            return EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(message.getText())
                    .entities(List.of(entity))
                    .replyMarkup(paginationKeyboard)
                    .build();
    }

    public EditMessageText saveMovieMessage(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData().split("\\|");
        long userId = callbackQuery.getFrom().getId();
        int currentPage = Integer.parseInt(data[current_page]);

        var movie = switch (data[state]) {
            case STATE.VIEWS_TOP -> movieService.getFromViewsTopByPosition(currentPage);
            case STATE.RATINGS_TOP -> movieService.getFromRatingsTopByPosition(currentPage);
            case STATE.RECOMMENDATION -> userService.findUserById(userId).getMoviesToRecommend().get(currentPage - 1);
            default -> throw new IllegalStateException("Unexpected value: " + data[state]);
        };
        userService.addMovieToUser(userId, movie, -1.0);

        var message = callbackQuery.getMessage();

        var editedMarkup = message.getReplyMarkup();
        var saveButton = editedMarkup.getKeyboard().get(0).get(1);
        saveButton.setText("Сохранено");
        saveButton.setCallbackData(ACTION.IGNORE);

        return EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(message.getText())
                .entities(message.getEntities())
                .replyMarkup(editedMarkup)
                .build();
    }

    public EditMessageText rateScaleMessage(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData().split("\\|");
        var message = callbackQuery.getMessage();
        int currentPage = Integer.parseInt(data[current_page]);
        String link = message.getEntities().get(0).getUrl();

        if (data[state].equals(STATE.MINE)) {
            var ratedMovies = userService.findUserById(callbackQuery.getFrom().getId()).getRatedMovies();

            if (ratedMovies == null || ratedMovies.size() == 0)
                return EditMessageText.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId())
                        .text("Список 'Мои фильмы' пуст")
                        .build();
        }
        return EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(message.getText())
                .replyMarkup(buttonDesigner.getScaleButtons(data[state], link, currentPage))
                .entities(message.getEntities())
                .build();
    }

    public EditMessageText rateMovieMessage(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData().split("\\|");
        long userId = callbackQuery.getFrom().getId();

        int maxPageNumber;
        int currentPage = Integer.parseInt(data[current_page]);
        double userRating = Double.parseDouble(data[rating]);

        var replyKeyboard = switch (data[state]) {
            case STATE.VIEWS_TOP -> {
                maxPageNumber = movieService.getViewsTopSize();
                Movie movie = movieService.getFromViewsTopByPosition(currentPage);
                userService.addMovieToUser(userId, movie, userRating);

                yield buttonDesigner.getPaginationKeyboardAfterRateAction(
                        data[state], movie.getLink(), currentPage, maxPageNumber, userRating
                );
            }
            case STATE.RATINGS_TOP -> {
                maxPageNumber = movieService.getRatingsTopSize();
                Movie movie = movieService.getFromRatingsTopByPosition(currentPage);
                userService.addMovieToUser(userId, movie, userRating);

                yield buttonDesigner.getPaginationKeyboardAfterRateAction(
                        data[state], movie.getLink(), currentPage, maxPageNumber, userRating
                );
            }
            case STATE.RECOMMENDATION -> {
                var moviesToRecommend = userService.findUserById(userId).getMoviesToRecommend();
                maxPageNumber = moviesToRecommend.size();
                Movie movie = moviesToRecommend.get(currentPage - 1);
                userService.addMovieToUser(userId, movie, userRating);

                if (maxPageNumber == 1) {
                    yield buttonDesigner.getKeyboardAfterRateAction(
                            data[state], movie.getLink(), currentPage, userRating);
                } else {
                    yield buttonDesigner.getPaginationKeyboardAfterRateAction(
                            data[state], movie.getLink(), currentPage, maxPageNumber, userRating);
                }
            }
            case STATE.MINE -> {
                var ratedMovies = userService.findUserById(userId).getRatedMovies();
                Movie movie = ratedMovies.get(currentPage - 1).getMovie();
                maxPageNumber = userService.addMovieToUser(userId, movie, userRating);

                if (maxPageNumber == 1) {
                    yield buttonDesigner.getKeyboardAfterRateAction(
                            data[state], movie.getLink(), currentPage, userRating);
                } else {
                    yield buttonDesigner.getPaginationKeyboardAfterRateAction(
                            data[state], movie.getLink(), currentPage, maxPageNumber, userRating);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + data[state]);
        };
        var message = callbackQuery.getMessage();

        return EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(message.getText())
                .entities(message.getEntities())
                .replyMarkup(replyKeyboard)
                .build();
    }

    public EditMessageText deleteMovieMessage(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData().split("\\|");
        long userId = callbackQuery.getFrom().getId();
        int currentPage = Integer.parseInt(data[current_page]);

        int moviesLeft = userService.deleteMovieFromUser(userId, currentPage - 1);

        if (moviesLeft > 0 || !data[state].equals(STATE.MINE)) {
            callbackQuery.setData(ACTION.PAGE_TURN + "|" + data[state] + "|r|" + (currentPage - 1));
            return editMessageOnPageTurn(callbackQuery);
        } else {
            var message = callbackQuery.getMessage();
            return EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text("Список 'Мои фильмы' пуст")
                    .build();
        }
    }
}