package com.example.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionBank(
    val topics: List<Topic> = emptyList(),
    val questions: List<Question> = emptyList(),
) {
    init {
        val topicIds = topics.map { it.id }
        require(topicIds.size == topicIds.toSet().size) { "В банке повторяются id тем" }

        val questionIds = questions.map { it.id }
        require(questionIds.size == questionIds.toSet().size) { "В банке повторяются id вопросов" }

        for (question in questions) {
            require(question.topicId in topicIds) {
                "Вопрос ${question.id} ссылается на несуществующую тему ${question.topicId}"
            }
        }
    }

    fun questionsOf(topicId: String): List<Question> = questions.filter { it.topicId == topicId }

    fun playableTopics(): List<Topic> = topics.filter { questionsOf(it.id).isNotEmpty() }
}
