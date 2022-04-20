package pt.isel.sample.parseWithJsonConvert.cake

import pt.isel.jsonConvert.JsonConvert
import pt.isel.sample.generalTests.Date

/**
 * Used to test parsing with JsonConvert annotation.
 * All parameters are optional.
 * All properties are "var".
 */
data class Cake7(
    @JsonConvert(ConverterWithoutInterface::class) var expDate: Date = Date(1, 1, 2022),
    var mainFlavor: String = "Cocoa"
)
