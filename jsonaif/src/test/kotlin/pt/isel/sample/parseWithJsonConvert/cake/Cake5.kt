package pt.isel.sample.parseWithJsonConvert.cake

import pt.isel.jsonConvert.JsonConvert
import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonConvert annotation.
 * One parameter is optional ([mainFlavor]).
 * All properties are "val".
 */
data class Cake5(
    @JsonConvert(ConverterNotObject::class) val expDate: Date,
    val mainFlavor: String = "Cocoa"
)
