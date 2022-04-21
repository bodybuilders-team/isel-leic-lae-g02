package pt.isel.sample.notParseable

/**
 * Not parseable class: one param (price) of the primary constructor is not a property.
 * No optional parameters.
 */
@Suppress("unused", "UNUSED_PARAMETER")
class Car(
    val license_plate: String,
    price: Int
)
