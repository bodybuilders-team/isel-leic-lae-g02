package pt.isel.sample

data class Bike(val plate: String, val model: String)

data class BikeWithOwner(val bike: Bike, val owner: Person)

data class Competition(val name: String, val competitors: List<BikeWithOwner>, val winner: Int)
