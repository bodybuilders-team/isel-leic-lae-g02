package pt.isel.json_parser


/**
 * An exception that is thrown when something unexpected happens when parsing.
 * @param message exception cause
 */
class ParseException(message: String) : Exception(message) {
	companion object {

		/**
		 * Receives an [unparsedValue] and a [className] and returns a ParseException with a specific message
		 * saying the value is not of the expected class.
		 * @param unparsedValue actual value
		 * @param className expected class
		 * @return ParseException with specific message
		 */
		fun unexpectedClass(unparsedValue: String, className: String) =
			ParseException("Value $unparsedValue is not of expected class $className")
	}
}
