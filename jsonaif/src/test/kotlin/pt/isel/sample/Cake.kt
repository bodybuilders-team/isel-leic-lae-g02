package pt.isel.sample

import pt.isel.json_convert.JsonConvert
import pt.isel.json_convert.JsonConverter

data class Cake(@JsonConvert(JsonToDate::class) val expDate: Date, val mainFlavor: String = "Cocoa")

object JsonToDate : JsonConverter<String, Date> {
    override fun convert(from: String): Date {
        val (year, month, day) = from.split("-")
        return Date(day.toInt(), month.toInt(), year.toInt())
    }
}
