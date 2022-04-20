package pt.isel.sample.parseWithJsonConvert.cake

import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonConvert annotation.
 * Object not implements the JsonConverter interface.
 */
object ConverterWithoutInterface {
    @Suppress("unused")
    fun convert(from: String): Date {
        val (year, month, day) = from.split("-")
        return Date(day.toInt(), month.toInt(), year.toInt())
    }
}
