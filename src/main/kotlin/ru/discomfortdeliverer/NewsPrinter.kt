package ru.discomfortdeliverer

import org.slf4j.LoggerFactory
import java.io.File
import ru.discomfortdeliverer.dto.news.News
import ru.discomfortdeliverer.service.convertMillisToLocalDateTime

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
}

// Функция для создания NewsPrinter
fun newsPrinter(block: NewsPrinter.() -> Unit): NewsPrinter {
    val printer = NewsPrinter()
    printer.block()
    return printer
}