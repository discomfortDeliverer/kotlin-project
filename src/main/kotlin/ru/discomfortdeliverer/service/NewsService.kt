package ru.discomfortdeliverer.service

import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.exp
import ru.discomfortdeliverer.dto.news.News

private val logger = LoggerFactory.getLogger("NewsService.kt")

fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    logger.info("Метод: getMostRatedNews(count, period). count={}, period={}", count, period)

    val mostRatedNews = this.asSequence()
        .filter { news ->
            val newsDate = convertMillisToLocalDateTime(news.publicationDate).toLocalDate()
            newsDate in period
        }
        .map { news ->
            news.rating = calculateRating(news.favoritesCount, news.commentsCount)
            news
        }
        .sortedByDescending { it.rating }
        .take(count)
        .toList()

    logger.info("Метод: getMostRatedNews(count, period). Размер отсортированного и отфильтрованного списка новостей={}", mostRatedNews.size)

    return mostRatedNews
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