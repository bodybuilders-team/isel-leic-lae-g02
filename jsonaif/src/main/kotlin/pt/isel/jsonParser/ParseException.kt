package pt.isel.jsonParser

/**
 * An exception that is thrown when something unexpected happens when parsing.
 *
 * @param message exception cause
 */
class ParseException(message: String) : Exception(message)
