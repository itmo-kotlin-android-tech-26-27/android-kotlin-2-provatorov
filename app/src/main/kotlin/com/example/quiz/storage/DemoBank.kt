package com.example.quiz.storage

import com.example.quiz.model.ChoiceQuestion
import com.example.quiz.model.MatchPair
import com.example.quiz.model.MatchQuestion
import com.example.quiz.model.OpenQuestion
import com.example.quiz.model.QuestionBank
import com.example.quiz.model.Topic

object DemoBank {
    private const val GEOGRAPHY = "geography"
    private const val BIOLOGY = "biology"

    fun create() = QuestionBank(
        topics = listOf(
            Topic(GEOGRAPHY, "География"),
            Topic(BIOLOGY, "Биология"),
        ),
        questions = listOf(
            ChoiceQuestion(
                id = "geography-1",
                topicId = GEOGRAPHY,
                level = 1,
                text = "Какой океан самый большой по площади?",
                options = listOf("Атлантический", "Тихий", "Индийский", "Северный Ледовитый"),
                correctIndex = 1,
            ),
            OpenQuestion(
                id = "geography-2",
                topicId = GEOGRAPHY,
                level = 1,
                text = "Какой город — столица Франции?",
                referenceAnswer = "Париж",
            ),
            ChoiceQuestion(
                id = "geography-3",
                topicId = GEOGRAPHY,
                level = 2,
                text = "На каком материке находится пустыня Сахара?",
                options = listOf("Азия", "Австралия", "Африка", "Южная Америка"),
                correctIndex = 2,
            ),
            MatchQuestion(
                id = "geography-4",
                topicId = GEOGRAPHY,
                level = 2,
                text = "Сопоставьте страну и её столицу",
                pairs = listOf(
                    MatchPair("Япония", "Токио"),
                    MatchPair("Канада", "Оттава"),
                    MatchPair("Австралия", "Канберра"),
                    MatchPair("Бразилия", "Бразилиа"),
                ),
            ),
            OpenQuestion(
                id = "geography-5",
                topicId = GEOGRAPHY,
                level = 3,
                text = "Какая река самая длинная в Европе?",
                referenceAnswer = "Волга",
            ),
            ChoiceQuestion(
                id = "geography-6",
                topicId = GEOGRAPHY,
                level = 3,
                text = "Какое озеро самое глубокое в мире?",
                options = listOf("Байкал", "Танганьика", "Виктория", "Верхнее"),
                correctIndex = 0,
            ),
            ChoiceQuestion(
                id = "biology-1",
                topicId = BIOLOGY,
                level = 1,
                text = "Какой орган перекачивает кровь по телу человека?",
                options = listOf("Лёгкие", "Печень", "Сердце", "Почки"),
                correctIndex = 2,
            ),
            OpenQuestion(
                id = "biology-2",
                topicId = BIOLOGY,
                level = 1,
                text = "Сколько ног у насекомых? Введите число цифрами.",
                referenceAnswer = "6",
            ),
            ChoiceQuestion(
                id = "biology-3",
                topicId = BIOLOGY,
                level = 2,
                text = "Какой газ растения поглощают при фотосинтезе?",
                options = listOf("Кислород", "Азот", "Углекислый газ", "Водород"),
                correctIndex = 2,
            ),
            MatchQuestion(
                id = "biology-4",
                topicId = BIOLOGY,
                level = 2,
                text = "Сопоставьте животное и класс, к которому оно относится",
                pairs = listOf(
                    MatchPair("Дельфин", "Млекопитающие"),
                    MatchPair("Лягушка", "Земноводные"),
                    MatchPair("Орёл", "Птицы"),
                    MatchPair("Акула", "Рыбы"),
                ),
            ),
            OpenQuestion(
                id = "biology-5",
                topicId = BIOLOGY,
                level = 3,
                text = "Как называется органоид растительной клетки, в котором идёт фотосинтез? Введите в единственном числе.",
                referenceAnswer = "хлоропласт",
            ),
            ChoiceQuestion(
                id = "biology-6",
                topicId = BIOLOGY,
                level = 3,
                text = "Сколько хромосом в клетках тела человека (кроме половых)?",
                options = listOf("23", "44", "46", "48"),
                correctIndex = 2,
            ),
        ),
    )
}
