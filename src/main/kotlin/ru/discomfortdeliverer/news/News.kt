package ru.discomfortdeliverer.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.discomfortdeliverer.news.Place

@Serializable
data class News(
    val id: Long,                       // Идентификатор новости
    val title: String,                  // Заголовок новости
    val place: Place?,                  // Место, где произошло событие
    val description: String,            // Описание новости
    @SerialName("site_url") val siteUrl: String,                // Ссылка на страницу новости на сайте KudaGo
    @SerialName("favorites_count") val favoritesCount: Int,            // Число пользователей, добавивших новость в избранное
    @SerialName("comments_count") val commentsCount: Int, // Число комментариев
    @Transient var rating: Double = 0.0,
    @SerialName("publication_date") val publicationDate: Long
)
