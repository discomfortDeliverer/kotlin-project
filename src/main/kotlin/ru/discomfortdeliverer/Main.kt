package ru.discomfortdeliverer

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import ru.discomfortdeliverer.client.getNews
import ru.discomfortdeliverer.client.getNewsByPages
import java.time.LocalDate
import ru.discomfortdeliverer.dto.news.News
import ru.discomfortdeliverer.service.convertMillisToLocalDateTime
import ru.discomfortdeliverer.service.getMostRatedNews

private val logger = LoggerFactory.getLogger("Main.kt")
fun main() = runBlocking {
    logger.info("App started")
    val newsList = getNews(30)

    val startDate = LocalDate.of(2024, 8, 1)
    val endDate = LocalDate.of(2024, 9, 1)
    val range = startDate..endDate

    val listOfAllNewsInRange = findAllNewsInRange(range)
    logger.info("Метод: main(). Размер списка со всеми новостями в промежутке={}, size={}",range, listOfAllNewsInRange.size)

    val mostRatedNews = listOfAllNewsInRange.getMostRatedNews(8, range)

    // Пример использования DSL для вывода новостей в консоль
    val printer = newsPrinter {
        mostRatedNews.forEach { news ->
            addNews(news)
        }
    }
    val newsFileSaver = newsFileSaver {
        mostRatedNews.forEach { news ->
            addNews(news)
        }
    }

    // Печать новостей в консоль
    printer.printToConsole()

    val scanner = java.util.Scanner(System.`in`)
    println("Введить путь к файлу куда сохранить новости:")
    val filePath = scanner.nextLine()
    logger.info("Метод: main(). Введенный путь для сохранения={}", filePath)

    // Сохранение новостей в файл
    newsFileSaver.saveToFile(filePath)
}

suspend fun findAllNewsInRange(period: ClosedRange<LocalDate>): List<News> {
    logger.info("Метод: findAllNewsInRange с параметром period={}", period)
    var listOfNewsInRange = ArrayList<News>()

    var i: Int = 1
    while (true) {
        val listNewsOnPage = getNewsByPages(i)

        for (news in listNewsOnPage) {
            var currentDate = convertMillisToLocalDateTime(news.publicationDate).toLocalDate()
            if (currentDate in period) {
                listOfNewsInRange.add(news)
            }
        }

        var lastDateOnPage = convertMillisToLocalDateTime(listNewsOnPage.get(listNewsOnPage.size-1).publicationDate).toLocalDate()
        if (lastDateOnPage.isBefore(period.start)) {
            break
        } else {
            i++
        }
    }
    return listOfNewsInRange
}