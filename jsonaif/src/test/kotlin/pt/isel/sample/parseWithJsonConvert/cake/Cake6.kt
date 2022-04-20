package pt.isel.sample.parseWithJsonConvert.cake

import pt.isel.jsonConvert.JsonConvert
import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonConvert annotation.
 * All parameters are optional.
 * All properties are "var".
 */
data class Cake6(
    @JsonConvert(ConverterMissingFunction::class) var expDate: Date = Date(1, 1, 2000),
    var mainFlavor: String = "Cocoa"
)
