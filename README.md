# Задание 2 – Квиз на экране Android-приложения

## Запуск

Нужны JDK 17 и Android SDK (платформа API 36). Проект открывается в IntelliJ IDEA с плагином Android
или в Android Studio: после открытия согласиться на загрузку Gradle-проекта, затем запустить
конфигурацию `app` на телефоне или эмуляторе.

Сборка из терминала:

```
./gradlew assembleDebug
```

Готовый файл: `app/build/outputs/apk/debug/app-debug.apk`.

Тесты логики:

```
./gradlew testDebugUnitTest
```

## Данные

При первом запуске приложение создает во внутренней памяти файлы `questions.json` (демо-банк),
`settings.json` и `attempts.json`. Если файл поврежден, приложение показывает сообщение и предлагает
сбросить его.

Выгрузка администратора сохраняется в папку "Загрузки" телефона: `quiz-export-<дата>.json`.

## Код

Все лежит в `app/src/main/kotlin/com/example/quiz/`:

- `model` – данные;
- `engine` – логика лестницы и проверка ответов;
- `storage` – чтение и запись JSON, выгрузка в "Загрузки";
- `ui` – экраны на Jetpack Compose: `login`, `student`, `quiz`, `admin`, `navigation`.
