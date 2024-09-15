package ru.discomfortdeliverer

import com.sun.java_cup.internal.runtime.Scanner
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import ru.discomfortdeliverer.news.News

fun main() = runBlocking {
    val newsList = getNews(30)

    val startDate = LocalDate.of(2024, 8, 1)
    val endDate = LocalDate.of(2024, 9, 1)
    val range = startDate..endDate

    val listOfAllNewsInRange = findAllNewsInRange(range)

    val mostRatedNews = listOfAllNewsInRange.getMostRatedNews(8, range)

    // Пример использования DSL для вывода новостей в консоль
    val printer = newsPrinter {
        mostRatedNews.forEach { news ->
            addNews(news)
        }
    }

    // Печать новостей в консоль
    printer.printToConsole()

    val scanner = java.util.Scanner(System.`in`)
    println("Введить путь к файлу куда сохранить новости:")
    val filePath = scanner.nextLine()

    // Сохранение новостей в файл
    printer.saveToFile(filePath)
}

suspend fun findAllNewsInRange(period: ClosedRange<LocalDate>): List<News> {
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