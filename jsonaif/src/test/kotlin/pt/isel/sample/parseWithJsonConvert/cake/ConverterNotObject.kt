package pt.isel.sample.parseWithJsonConvert.cake

import pt.isel.jsonConvert.JsonConverter
import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonConvert annotation.
 * Implements JsonConverter, but it's not an object.
 */
class ConverterNotObject : JsonConverter<String, Date> {
    override fun convert(from: String): Date {
        val (year, month, day) = from.split("-")
        return Date(day.toInt(), month.toInt(), year.toInt())
    }
}
