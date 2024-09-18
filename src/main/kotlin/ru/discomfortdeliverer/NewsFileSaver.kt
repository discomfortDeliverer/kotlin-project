package ru.discomfortdeliverer

import org.slf4j.LoggerFactory
import ru.discomfortdeliverer.dto.news.News
import ru.discomfortdeliverer.service.convertMillisToLocalDateTime
import java.io.File

class NewsFileSaver {
    private val logger = LoggerFactory.getLogger("NewsFileSaver.kt")
    private val newsList = mutableListOf<News>()

    fun addNews(news: News) {
        newsList.add(news)
    }
    fun saveToFile(filePath: String) {
        val file = File(filePath)



        // Проверка на валидность пути
        if (!file.parentFile.exists()) {
            println("Ошибка: Директория '${file.parent}' не существует.")
            logger.error("Метод: saveToFile(filePath) Ошибка - Директория '{}' не существует", file.parent)
            return
        }

        // Проверка на существование файла
        if (file.exists()) {
            println("Ошибка: Файл '${file.name}' уже существует. Пожалуйста, выберите другое имя или путь.")
            logger.error("Метод: saveToFile(filePath) Ошибка - Файл '{}' уже существует", file.name)
            return
        }

        try {
            file.printWriter().use { writer ->
                newsList.forEach { news ->
                    writer.println("ID: ${news.id}")
                    writer.println("Title: ${news.title}")
                    writer.println("Place: ${news.place?.id ?: "N/A"}")
                    writer.println("Description: ${news.description}")
                    writer.println("Site URL: ${news.siteUrl}")
                    writer.println("Favorites: ${news.favoritesCount}, Comments: ${news.commentsCount}")
                    writer.println("Rating: ${news.rating}")
                    writer.println("Publication date: ${convertMillisToLocalDateTime(news.publicationDate)}")
                    writer.println("---")
                }
            }
            logger.info("Метод: saveToFile(filePath) Новости сохранены в файл={}", file.name)
            println("Новости сохранены в $filePath")
        } catch (e: Exception) {
            println("Ошибка при сохранении файла: ${e.message}")
            logger.error("Метод: saveToFile(filePath) Ошибка при сохранении файла", e)
        }
    }
}
fun newsFileSaver(block: NewsFileSaver.() -> Unit): NewsFileSaver {
    val newsFileSaver = NewsFileSaver()
    newsFileSaver.block()
    return newsFileSaver
}