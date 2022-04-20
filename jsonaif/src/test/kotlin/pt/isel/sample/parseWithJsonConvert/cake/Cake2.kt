package pt.isel.sample.parseWithJsonConvert.cake

import pt.isel.jsonConvert.JsonConvert
import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonConvert annotation.
 * One parameter is optional ([mainFlavor]).
 * All properties are "val".
 */
data class Cake2(
    @JsonConvert(JsonToDate::class) val expDate: Date,
    val mainFlavor: String = "Cocoa"
)
