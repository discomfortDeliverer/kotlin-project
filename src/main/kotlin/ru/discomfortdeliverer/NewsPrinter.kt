package ru.discomfortdeliverer

import org.slf4j.LoggerFactory
import java.io.File
import ru.discomfortdeliverer.news.News
import java.nio.file.Paths

private val logger = LoggerFactory.getLogger("NewsPrinter.kt")
// Определяем класс NewsPrinter для DSL
class NewsPrinter {
    private val newsList = mutableListOf<News>()

    fun addNews(news: News) {
        newsList.add(news)
    }

    fun printToConsole() {
        newsList.forEach { news ->
            println("ID: ${news.id}")
            println("Publication date: ${news.publicationDate}")
            println("Title: ${news.title}")
            println("Place: ${news.place?.id ?: "N/A"}")
            println("Description: ${news.description}")
            println("Site URL: ${news.siteUrl}")
            println("Favorites: ${news.favoritesCount}, Comments: ${news.commentsCount}")
            println("Rating: ${news.rating}")
            println("Publication date: ${convertMillisToLocalDateTime(news.publicationDate)}")
            println("---")
        }
    }

    fun saveToFile(filePath: String) {
        val file = File(filePath)

        // Проверка на валидность пути
        if (!file.parentFile.exists()) {
            println("Ошибка: Директория '${file.parent}' не существует.")
            logger.warn("Метод: saveToFile(filePath) Ошибка - Директория '{}' не существует", file.parent)
            return
        }

        // Проверка на существование файла
        if (file.exists()) {
            println("Ошибка: Файл '${file.name}' уже существует. Пожалуйста, выберите другое имя или путь.")
            logger.warn("Метод: saveToFile(filePath) Ошибка - Файл '{}' уже существует", file.name)
            return
        }

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
        println("News saved to $filePath")
    }
}

// Функция для создания NewsPrinter
fun newsPrinter(block: NewsPrinter.() -> Unit): NewsPrinter {
    val printer = NewsPrinter()
    printer.block()
    return printer
}