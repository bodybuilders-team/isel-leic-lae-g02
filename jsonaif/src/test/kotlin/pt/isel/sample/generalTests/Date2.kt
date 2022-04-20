package pt.isel.sample.generalTests

/**
 * Used to test parsing with JsonConvert and JsonProperty annotations.
 * All optional parameters.
 * All properties are "var".
 */
data class Date2(
    var day: Int = 1,
    var month: Int = 1,
    var year: Int = 2022
)
