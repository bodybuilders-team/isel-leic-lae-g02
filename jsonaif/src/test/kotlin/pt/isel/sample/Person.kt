package pt.isel.sample

data class Person (val id: Int=0, val name: String, val birth: Date? = null, var sibling: Person? = null)
