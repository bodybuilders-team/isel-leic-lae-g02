package pt.isel

import sun.misc.Unsafe

object UnsafeUtils {
    @JvmField
    val unsafe = getNewUnsafe()

    @JvmStatic
    fun getNewUnsafe(): Unsafe {
        val f = Unsafe::class.java.getDeclaredField("theUnsafe")
        f.isAccessible = true
        return f[null] as Unsafe
    }
}
