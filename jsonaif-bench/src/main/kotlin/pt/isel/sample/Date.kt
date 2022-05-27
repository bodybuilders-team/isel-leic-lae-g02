package pt.isel.sample

/**
 * Class used for benchmarking purposes.
 * All parameters are optional and primitive types.
 */
data class Date(
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0
)

/**
 * Class used for benchmarking purposes.
 * All parameters are optional and reference types.
 */
data class Date2(
    var day: String = "",
    var month: String = "",
    var year: String = ""
)

/**
 * Class used for benchmarking purposes.
 * All parameters are required and primitive types.
 */
data class ConstantDate(
    val day: Int,
    val month: Int,
    val year: Int
)

/**
 * Class used for benchmarking purposes.
 * All parameters are required and reference types.
 */
data class ConstantDate2(
    val day: String,
    val month: String,
    val year: String
)
