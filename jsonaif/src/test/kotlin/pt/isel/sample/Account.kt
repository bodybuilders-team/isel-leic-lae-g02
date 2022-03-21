package pt.isel.sample

data class Transaction(val fromId: String, val toId: String, val amount: Double)
data class Account(val id: String, var balance: Double, val transactions: List<Transaction>)
