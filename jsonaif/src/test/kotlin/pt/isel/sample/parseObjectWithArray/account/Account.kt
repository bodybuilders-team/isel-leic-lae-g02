package pt.isel.sample.parseObjectWithArray.account

/**
 * Used to test parsing an object (Account) with an array (list os Transaction).
 * No optional parameters.
 */
data class Account(
    val id: String,
    var balance: Double,
    val transactions: List<Transaction>
)
