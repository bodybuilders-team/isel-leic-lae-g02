package pt.isel.sample.jsonconvert

import pt.isel.jsonConvert.JsonConvert
import pt.isel.jsonConvert.JsonConverter
import pt.isel.sample.Date

data class Cake(@JsonConvert(JsonToDate::class) var expDate: Date = Date(1, 1, 2000), var mainFlavor: String = "Cocoa")

data class Cake2(@JsonConvert(JsonToDate::class) val expDate: Date, val mainFlavor: String = "Cocoa")

object JsonToDate : JsonConverter<String, Date> {
    override fun convert(from: String): Date {
        val (year, month, day) = from.split("-")
        return Date(day.toInt(), month.toInt(), year.toInt())
    }
}

data class Cake3(@JsonConvert(ConverterMissingFunction::class) val expDate: Date, val mainFlavor: String = "Cocoa")

object ConverterMissingFunction

data class Cake4(@JsonConvert(ConverterWithoutInterface::class) val expDate: Date, val mainFlavor: String = "Cocoa")

object ConverterWithoutInterface {
    fun convert(from: String): Date {
        val (year, month, day) = from.split("-")
        return Date(day.toInt(), month.toInt(), year.toInt())
    }
}

data class Cake5(@JsonConvert(ConverterNotObject::class) val expDate: Date, val mainFlavor: String = "Cocoa")

class ConverterNotObject : JsonConverter<String, Date> {
    override fun convert(from: String): Date {
        val (year, month, day) = from.split("-")
        return Date(day.toInt(), month.toInt(), year.toInt())
    }
}
