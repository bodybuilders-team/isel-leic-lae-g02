package pt.isel.utils

import sun.misc.Unsafe

/**
 * A utility class to access the Unsafe object.
 *
 * @property unsafe the Unsafe object
 */
object UnsafeUtils {

    @JvmField
    val unsafe = getNewUnsafe()

    /**
     * Returns a new instance of the Unsafe object.
     *
     * @return Unsafe object.
     */
    @JvmStatic
    fun getNewUnsafe(): Unsafe {
        val f = Unsafe::class.java.getDeclaredField("theUnsafe")
        f.isAccessible = true
        return f[null] as Unsafe
    }
}
