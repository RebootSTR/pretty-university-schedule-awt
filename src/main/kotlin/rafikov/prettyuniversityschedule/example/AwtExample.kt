package rafikov.prettyuniversityschedule.example

import rafikov.prettyuniversityschedule.builder.ScheduleBuilder
import rafikov.prettyuniversityschedule.drawer.implementation.AwtDrawer
import rafikov.prettyuniversityschedule.models.domain.Lesson
import rafikov.prettyuniversityschedule.models.domain.LessonType
import java.io.File

/**
 *
 * @author Aydar Rafikov
 */
fun main(args: Array<String>) {
    val drawer = AwtDrawer(schedule)
    drawer.drawSchedule()
    drawer.save(File("Example.png"))
}

val schedule = ScheduleBuilder("ИБ-91 (3-16)").build {
    addTimes {
        add("8:10-9:45")
        add("9:55-11:30")
        add("11:40-13:15")
        add("13:35-15:10")
        add("15:20-16:55")
    }
    addDay(3) {
        block(
            number = 1,
            both = Lesson("Математика (6-10)", type = LessonType.LAB, teacher = "Иванов Ю.В.")
        )
        block(
            number = 4,
            odd = Lesson("Физ-ра (электив)", type = LessonType.PRACTICE, teacher = "")
        )
        block(
            number = 5,
            even = Lesson("Физ-ра", type = LessonType.PRACTICE, teacher = "Геннадьева"),
            odd = Lesson("Физ-ра (электив)", type = LessonType.PRACTICE, teacher = "John Doe")
        )
    }
    addDay(6) {}
}