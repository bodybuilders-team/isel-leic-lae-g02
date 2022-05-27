package pt.isel.sample

/**
 * Class used for benchmarking purposes.
 * All parameters are optional and reference types.
 */
data class Classroom(
    var students: List<Student> = emptyList(),
)
