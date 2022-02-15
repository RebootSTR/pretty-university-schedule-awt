package json.parser

import ScheduleBuilder
import com.google.gson.Gson
import drawer.Lesson
import drawer.LessonType
import json.Schedule
import java.io.File

/**
 *
 * @author Aydar Rafikov
 */
object ScheduleParser {

    private val gson = Gson()

    fun parseFromFile(filename: String): drawer.Schedule {
        val json = File(filename).readText()
        val jsonSchedule = gson.fromJson(json, Schedule::class.java)
        return buildSchedule(jsonSchedule)
    }

    private fun buildSchedule(jsonSchedule: Schedule) =
        ScheduleBuilder(jsonSchedule.groupName).build {
            addTimes {
                for (time in jsonSchedule.times) {
                    add(time)
                }
            }

            for (day in jsonSchedule.days) {
                addDay(day.number, day.remote) {
                    for (l in day.lessons) {
                        block(
                            l.number,
                            l.even?.let { Lesson(it.name, LessonType.valueOf(it.type), it.teacher ?: "") },
                            l.odd?.let { Lesson(it.name, LessonType.valueOf(it.type), it.teacher ?: "") },
                            l.both?.let { Lesson(it.name, LessonType.valueOf(it.type), it.teacher ?: "") }
                        )
                    }
                }
            }
        }
}