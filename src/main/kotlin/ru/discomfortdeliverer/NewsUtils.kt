package ru.discomfortdeliverer

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.exp
import ru.discomfortdeliverer.news.News
    fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
        // Фильтрация новостей по периоду времени
        val filteredNews = this.filter { news ->
            val newsDate = convertMillisToLocalDateTime(news.publicationDate).toLocalDate()
            newsDate in period
        }


        // Расчет рейтинга и сортировка
        for (news in filteredNews) {
            news.rating = calculateRating(news.favoritesCount, news.commentsCount)
        }

        // Возвращаем не более count новостей
        return filteredNews.sortedByDescending { it.rating }.take(count)
    }

    fun convertMillisToLocalDateTime(seconds: Long): LocalDateTime {

        val instant = Instant.ofEpochSecond(seconds)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    private fun calculateRating(favoritesCount: Int, commentsCount: Int): Double {
        return 1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }
