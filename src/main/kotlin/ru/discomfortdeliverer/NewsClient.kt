package ru.discomfortdeliverer

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import ru.discomfortdeliverer.news.News
import ru.discomfortdeliverer.news.NewsResponse

private val logger = LoggerFactory.getLogger("NewsClient.kt")
suspend fun getNews(count: Int = 100): List<News> {
    logger.debug("Метод: getNews(count) count={}", count)
    val client = HttpClient(CIO)
    val location = "spb"
    val url = "https://kudago.com/public-api/v1.4/news/"

    return try {
        val response: HttpResponse = client.get(url) {
            parameter("fields", "id,title,place,description,site_url,favorites_count,comments_count,publication_date")
            parameter("page", 1)
            parameter("page_size", count)
            parameter("order_by", "-publication_date")
            parameter("location", location)
        }

        if (response.status.isSuccess()) {
            val jsonResponse = response.bodyAsText()
            val newsResponse = Json.decodeFromString<NewsResponse>(jsonResponse)
            logger.info("Метод: getNews(count). Размер списка новостей ={}", newsResponse.results.size)
            newsResponse.results
        } else {
            logger.error("Метод: getNews(count). Ошибка HTTP: {}", response.status)
            emptyList() // Возвращаем пустой список в случае ошибки
        }
    } catch (e: Exception) {
        logger.error("Метод: getNews(count). Произошла ошибка: {}", e.message, e)
        emptyList() // Возвращаем пустой список в случае исключения
    } finally {
        client.close() // Закрываем клиент в блоке finally
    }
}

suspend fun getNewsByPages(page: Int): List<News> {
    logger.info("Метод: getNewsByPages(page) page={}", page)
    val client = HttpClient(CIO)
    val location = "spb"
    val url = "https://kudago.com/public-api/v1.4/news/"

    return try {
        val response: HttpResponse = client.get(url) {
            parameter("fields", "id,title,place,description,site_url,favorites_count,comments_count,publication_date")
            parameter("page", page)
            parameter("page_size", "100")
            parameter("order_by", "-publication_date")
            parameter("location", location)
        }

        if (response.status.isSuccess()) {
            val jsonResponse = response.bodyAsText()
            val newsResponse = Json.decodeFromString<NewsResponse>(jsonResponse)
            logger.info("Метод: getNewsByPages(page). Размер полученного списка новостей ={}", newsResponse.results.size)
            newsResponse.results
        } else {
            logger.error("Метод: getNewsByPages(page). Ошибка HTTP: {}", response.status)
            emptyList() // Возвращаем пустой список в случае ошибки
        }
    } catch (e: Exception) {
        logger.error("Метод: getNewsByPages(page). Произошла ошибка: {}", e.message, e)
        emptyList() // Возвращаем пустой список в случае исключения
    } finally {
        client.close() // Закрываем клиент в блоке finally
    }
}