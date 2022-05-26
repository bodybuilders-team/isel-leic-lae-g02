package pt.isel

import pt.isel.jsonParser.parsers.reflect.JsonParserReflect
import pt.isel.sample.generalTests.student.Student
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseFolderTests {

    @BeforeTest
    fun resetFiles() {
        val filePath =
            "C:\\ISEL\\4º Semestre\\LAE\\jsonaif-i41d_02\\jsonaif\\src\\test\\kotlin\\pt\\isel\\sample\\parseFolder\\test1"

        val file1 = File("$filePath\\test1.txt")
        val file2 = File("$filePath\\test1_2.txt")
        val file3 = File("$filePath\\test1_3.txt")

        file1.writeText("{}")
        file2.writeText("{}")
        file3.writeText("{}")

        assertEquals("{}", file1.readText())
        assertEquals("{}", file2.readText())
        assertEquals("{}", file3.readText())
    }

    @Test
    fun `parseFolderEager works`() {
        val students =
            JsonParserReflect.parseFolderEager<Student>("C:\\ISEL\\4º Semestre\\LAE\\jsonaif-i41d_02\\jsonaif\\src\\test\\kotlin\\pt\\isel\\sample\\parseFolder\\test1")

        assertEquals(
            listOf(
                Student(nr = 0, name = null),
                Student(nr = 0, name = null),
                Student(nr = 0, name = null)
            ),
            students
        )
    }

    @Test
    fun `parseFolderLazy works`() {
        val students =
            JsonParserReflect.parseFolderLazy<Student>("C:\\ISEL\\4º Semestre\\LAE\\jsonaif-i41d_02\\jsonaif\\src\\test\\kotlin\\pt\\isel\\sample\\parseFolder\\test1")

        assertEquals(
            listOf(
                Student(nr = 0, name = null),
                Student(nr = 0, name = null),
                Student(nr = 0, name = null)
            ),
            students.toList()
        )
    }

    @Test
    fun `Changing file during parsing with parseFolderEager`() {
        val filePath =
            "C:\\ISEL\\4º Semestre\\LAE\\jsonaif-i41d_02\\jsonaif\\src\\test\\kotlin\\pt\\isel\\sample\\parseFolder\\test1"

        val file2 = File("$filePath\\test1_2.txt")
        val file3 = File("$filePath\\test1_3.txt")

        val students =
            JsonParserReflect.parseFolderEager<Student>(filePath)

        assertEquals(
            listOf(
                Student(nr = 0, name = null),
                Student(nr = 0, name = null),
                Student(nr = 0, name = null)
            ),
            students.toList()
        )

        val studentsIterator = students.iterator()

        assertEquals(Student(nr = 0, name = null), studentsIterator.next())

        file2.writeText("{ nr: 0, name: \"test1_2\" }")

        assertEquals(Student(nr = 0, name = null), studentsIterator.next())

        file3.writeText("{ nr: 0, name: \"test1_3\" }")

        assertEquals(Student(nr = 0, name = null), studentsIterator.next())

        assertEquals(
            listOf(
                Student(nr = 0, name = null),
                Student(nr = 0, name = null),
                Student(nr = 0, name = null)
            ),
            students.toList()
        )
    }

    @Test
    fun `Changing file during parsing with parseFolderLazy`() {
        val filePath =
            "C:\\ISEL\\4º Semestre\\LAE\\jsonaif-i41d_02\\jsonaif\\src\\test\\kotlin\\pt\\isel\\sample\\parseFolder\\test1"

        val file2 = File("$filePath\\test1_2.txt")
        val file3 = File("$filePath\\test1_3.txt")

        val students =
            JsonParserReflect.parseFolderLazy<Student>(filePath)

        assertEquals(
            listOf(
                Student(nr = 0, name = null),
                Student(nr = 0, name = null),
                Student(nr = 0, name = null)
            ),
            students.toList()
        )

        val studentsIterator = students.iterator()

        assertEquals(Student(nr = 0, name = null), studentsIterator.next())

        file2.writeText("{ nr: 2, name: \"test1_2\" }")

        assertEquals(Student(nr = 2, name = "test1_2"), studentsIterator.next())

        assertEquals(
            listOf(
                Student(nr = 0, name = null),
                Student(nr = 2, name = "test1_2"),
                Student(nr = 0, name = null)
            ),
            students.toList()
        )

        file3.writeText("{ nr: 3, name: \"test1_3\" }")

        assertEquals(Student(nr = 3, name = "test1_3"), studentsIterator.next())

        assertEquals(
            listOf(
                Student(nr = 0, name = null),
                Student(nr = 2, name = "test1_2"),
                Student(nr = 3, name = "test1_3")
            ),
            students.toList()
        )
    }
}
