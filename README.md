# Recommender Bot

Данный проект представляет собой рекомендательную систему

## **Модуль рекомендательной системы (**[`Recommendations`](Recommendations)**)**

* **Алгоритмы рекомендаций (**[`algorithms`](Recommendations/src/main/java/com/recommender/bot/algorithms)**)**
    * _Совместная (коллаборативная)
      фильтрация_ ([`memory`](Recommendations/src/main/java/com/recommender/bot/algorithms/memory))\
      `UserBased` и `ItemBased` - реализации алгоритма совместной фильтрации по пользователям и предметам
      соответственно.<br/><br/>

    * _Кластеризация методом k-средних_ ([`model`](Recommendations/src/main/java/com/recommender/bot/algorithms/model))\
      `ItemClustering` - реализация алгоритма кластеризации методом k-средних.<br/><br/>

    * _Рейтинговые
      рекомендации_ ([`popularity`](Recommendations/src/main/java/com/recommender/bot/algorithms/popularity))\
      `PopularityBased` - реализация рейтинга фильмов по формуле `(R * V + C * M) / (V + M)`, где \
      V - число голосов, отданных за фильм;\
      M - минимальное количество голосов для включения в рейтинг;\
      R - средняя оценка фильма (по десятибалльной шкале);\
      C - средняя оценка среди всех фильмов.<br/><br/>

* **Конфигурация и выполнение рекомендаций (
  **[`recommendations`](Recommendations/src/main/java/com/recommender/bot/service/recommendations)**)**
    * `AlgorithmsConfig` - класс конфигурации рекомендательной системы.<br/><br/>
    * `Recommendations` - класс, определяющий набор алгоритмов рекомендации.<br/><br/>

* **Контроллеры (**[`controller`](Recommendations/src/main/java/com/recommender/bot/controller)**)**
    * `ConfigurationController` - контроллер, отвечающий за изменение конфигурации рекомендательной системы.<br/><br/>
    * `DataController` - контроллер, отвечающий за передачу данных в рекомендательную систему.<br/><br/>
    * `RecommendationsController` - контроллер, принимающий запросы на отправку рекомендации.<br/><br/>

## **Модуль Telegram бота (**[`TelegramBot`](TelegramClient)**)**
![Команды](images/commands.png "Команды") ![Команды](images/commands.png "Команды")