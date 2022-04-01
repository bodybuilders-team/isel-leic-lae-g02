package pt.isel.jsonConvert

/**
 * JSON converter from a type K to type T.
 */
interface JsonConverter<K, T> {

    /**
     * Convert function.
     * @param from value to convert
     */
    fun convert(from: K): T
}
