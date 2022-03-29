package pt.isel.sample

data class Person(val id: Int=0, val name: String, val birth: Date? = null,val sibling: Person? = null)
