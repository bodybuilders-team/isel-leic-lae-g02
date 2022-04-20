package pt.isel.sample.parseWithJsonConvert.cake

import pt.isel.jsonConvert.JsonConverter
import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonConvert annotation.
 * JsonConverter well implemented.
 */
object JsonToDate : JsonConverter<String, Date> {
    override fun convert(from: String): Date {
        val (year, month, day) = from.split("-")
        return Date(day.toInt(), month.toInt(), year.toInt())
    }
}
