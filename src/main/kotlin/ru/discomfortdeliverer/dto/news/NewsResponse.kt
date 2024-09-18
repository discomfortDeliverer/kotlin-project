package ru.discomfortdeliverer.dto.news

import kotlinx.serialization.Serializable
import ru.discomfortdeliverer.dto.news.News

@Serializable
data class NewsResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<News>
)
