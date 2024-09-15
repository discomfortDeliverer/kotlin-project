package ru.discomfortdeliverer

import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.exp
import ru.discomfortdeliverer.news.News

private val logger = LoggerFactory.getLogger("NewsUtils.kt")

fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    logger.info("Метод: getMostRatedNews(count, period). count={}, period={}", count, period)
    // Фильтрация новостей по периоду времени
    val filteredNews = this.filter { news ->
        val newsDate = convertMillisToLocalDateTime(news.publicationDate).toLocalDate()
        newsDate in period
    }

    // Расчет рейтинга и сортировка
    for (news in filteredNews) {
        news.rating = calculateRating(news.favoritesCount, news.commentsCount)
    }

    logger.info("Метод: getMostRatedNews(count, period). Размер отсортированного и отфильтрованного" +
            " списка новостей={}", filteredNews.size)
    // Возвращаем не более count новостей
    return filteredNews.sortedByDescending { it.rating }.take(count)
}

fun convertMillisToLocalDateTime(seconds: Long): LocalDateTime {
    logger.trace("Метод: convertMillisToLocalDateTime(seconds), seconds={}",seconds)
    val instant = Instant.ofEpochSecond(seconds)
    val convertedValue = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    logger.trace("Метод: convertMillisToLocalDateTime(seconds), convertedValue={}",convertedValue)
    return convertedValue
}

private fun calculateRating(favoritesCount: Int, commentsCount: Int): Double {
    return 1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
}