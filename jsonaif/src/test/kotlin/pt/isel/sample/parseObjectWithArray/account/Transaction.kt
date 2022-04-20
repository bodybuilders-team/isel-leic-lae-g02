package pt.isel.sample.parseObjectWithArray.account

/**
 * Used in Account (list of transactions).
 * No optional parameters.
 */
data class Transaction(
    val fromId: String,
    val toId: String,
    val amount: Double
)
