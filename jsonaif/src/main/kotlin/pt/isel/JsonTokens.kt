package pt.isel

const val OBJECT_OPEN = '{'
const val OBJECT_END = '}'
const val ARRAY_OPEN = '['
const val ARRAY_END = ']'
const val DOUBLE_QUOTES = '"'
const val COMMA = ','
const val COLON = ':'

/**
 * Token iterator for a JSON string.
 *
 * @param json JSON string
 * @property src JSON string as char array
 * @property index current char index
 * @property current current char
 */
class JsonTokens(json: String) {
    private val src = json.toCharArray()
    private var index = 0
    val current: Char
        get() = src[index]

    /**
     * Tries to advance in the [src] indexes.
     *
     * @return true if the advance succeeded
     */
    private fun tryAdvance(): Boolean {
        index++
        return index < src.size
    }

    /**
     * Trims the [src].
     */
    fun trim() {
        while (src[index] == ' ')
            if (!tryAdvance())
                break
    }

    /**
     * Pops a char from [src] and advances.
     *
     * @return popped char
     */
    @Suppress("unused")
    fun pop(): Char {
        val token = src[index]
        index++
        return token
    }

    /**
     * Pops the [expected] char from [src] and advances.
     *
     * @param expected expected char to pop
     *
     * @return popped char
     * @throws Exception if the current char is not the expected
     */
    fun pop(expected: Char) {
        if (current != expected)
            throw Exception("Expected $expected but found $current")
        index++
    }

    /**
     * Pops a word finished with [delimiter].
     *
     * @param delimiter word delimiter
     * @return popped word
     */
    fun popWordFinishedWith(delimiter: Char): String {
        trim()
        var acc = ""
        while (current != delimiter) {
            acc += current
            tryAdvance()
        }
        tryAdvance() // Discard delimiter
        trim()
        return acc
    }

    /**
     * Pops a primitive word.
     *
     * @return popped word
     */
    fun popWordPrimitive(): String {
        trim()
        var acc = ""
        while (!isEnd(current)) {
            acc += current
            tryAdvance()
        }
        trim()
        return acc
    }

    /**
     * Checks if the current char is a delimiter.
     *
     * @param curr current char
     * @return true if the current char is a delimiter
     */
    private fun isEnd(curr: Char): Boolean =
        curr == OBJECT_END || curr == ARRAY_END || curr == COMMA
}
