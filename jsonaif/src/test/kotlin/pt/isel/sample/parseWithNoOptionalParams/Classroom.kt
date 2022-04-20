package pt.isel.sample.parseWithNoOptionalParams

import pt.isel.sample.generalTests.student.Student

/**
 * Used to test parsing an object with no optional parameters.
 * No optional parameters.
 */
data class Classroom(
    val name: String,
    val students: List<Student>
)
