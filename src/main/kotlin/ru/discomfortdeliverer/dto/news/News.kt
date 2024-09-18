package ru.discomfortdeliverer.dto.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.discomfortdeliverer.dto.news.Place

@Serializable
data class News(
    val id: Long,
    val title: String,
    val place: Place?,
    val description: String,
    @SerialName("site_url") val siteUrl: String,
    @SerialName("favorites_count") val favoritesCount: Int,
    @SerialName("comments_count") val commentsCount: Int,
    @Transient var rating: Double = 0.0,
    @SerialName("publication_date") val publicationDate: Long
)
