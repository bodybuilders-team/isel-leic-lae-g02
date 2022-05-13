package pt.isel

data class Date(
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0
)

data class Date2(
    var day: String = "",
    var month: String = "",
    var year: String = ""
)

data class ConstantDate(
    val day: Int,
    val month: Int,
    val year: Int
)

data class ConstantDate2(
    val day: String,
    val month: String,
    val year: String
)
