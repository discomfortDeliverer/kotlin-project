package ru.discomfortdeliverer.news

import kotlinx.serialization.Serializable
import ru.discomfortdeliverer.news.News

@Serializable
data class NewsResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<News>
)
