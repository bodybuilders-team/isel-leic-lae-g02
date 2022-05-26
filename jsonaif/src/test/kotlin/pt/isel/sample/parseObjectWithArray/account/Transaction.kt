package pt.isel.sample.parseObjectWithArray.account

/**
 * Used in Account2 (list of transactions).
 * All optional parameters.
 * All properties are "var".
 */
data class Transaction(
    var fromId: String = "123",
    var toId: String = "321",
    var amount: Double = 15.0
)
