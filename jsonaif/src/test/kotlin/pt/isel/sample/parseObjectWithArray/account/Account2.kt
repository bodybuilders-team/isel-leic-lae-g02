package pt.isel.sample.parseObjectWithArray.account

/**
 * Used to test parsing an object (Account) with an array (list os Transaction).
 * All optional parameters.
 * All properties are "var".
 */
data class Account2(
    var id: String = "123",
    var balance: Double = 100.0,
    var transactions: List<Transaction2> = emptyList()
)
