
import drawer.ScheduleDrawer
import json.parser.ScheduleParser
import java.io.File

/**
 *
 * @author Aydar Rafikov
 */

class Main {
    companion object {
        const val RPIS_91 = "РПИС-91"

        @JvmStatic
        fun main(args: Array<String>) {
            var name: String?
            while (true) {
                println("Введите имя json файла (без .json)")
                name = readLine()
                if (name == "") {
                    name = RPIS_91
                }
                if (File("$name.json").exists()) {
                    break
                }
                println("Файл $name.json не найден, попробуйте еще раз")
            }
            val schedule = ScheduleParser.parseFromFile("$name.json")
            // Создание
            val drawer = ScheduleDrawer(schedule)
            drawer.drawSchedule()
            drawer.save("$name")
        }

        fun forTest() = ScheduleBuilder("хуй]").build {
            addTimes {
                add("8:10-9:45")
                add("9:55-11:30")
                add("11:40-13:15")
                add("13:35-15:10")
                add("15:20-16:55")
            }
            addDay(1) {}
            addDay(6) {}
        }
    }
}

//    val schedule = ScheduleBuilder("РПИС-92").build {
//        addTimes {
//            add("8:10-9:45")
//            add("9:55-11:30")
//            add("11:40-13:15")
//            add("13:35-15:10")
//            add("15:20-16:55")
//        }
////        addDay(1) {
////            lesson(1, even = null, odd = null, both = null, type = Lesson.Type.LECTURE)
////        }
//        addDay(1) {
//            lesson(4, both = "Менеджмент (2-07)")
//            lesson(5, both = "Физика (2-08)")
//        }
//        addDay(2) {
//            lesson(3, even = null, odd = null, both = "Физ-ра", type = Lesson.Type.PRACTICE)
//            lesson(4, even = null, odd = null, both = "Физ-ра (электив)", type = Lesson.Type.PRACTICE)
//        }
//        addDay(3) {
//            lesson(1, even = null, odd = null, both = "Физ-ра", type = Lesson.Type.PRACTICE)
//            lesson(2, even = null, odd = null, both = "Физ-ра (электив)", type = Lesson.Type.PRACTICE)
//            lesson(3, even = null, odd = null, both = "Иностранный язык (8-02)", type = Lesson.Type.PRACTICE)
//        }
//        addDay(4) {
//            lesson(1, even = null, odd = null, both = "Управл. треб. к прог. обесп. (2-06)", type = Lesson.Type.LECTURE)
//            lesson(2, even = null, odd = null, both = "Информатика (2-06)", type = Lesson.Type.LECTURE)
//            lesson(3, even = null, odd = null, both = "Программирование (2-06)", type = Lesson.Type.LECTURE)
//        }
//        addDay(5,) {
//            lesson(2, even = null, odd = null, both = "Математика (2-08)", type = Lesson.Type.LECTURE)
//            lesson(3, even = null, odd = null, both = "Информатика (2-08)", type = Lesson.Type.LECTURE)
//        }
//        addDay(6) {}
//    }
//