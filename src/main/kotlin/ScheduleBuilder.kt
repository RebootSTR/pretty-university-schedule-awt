import drawer.*

/**
 *
 * @author Aydar Rafikov
 */

val days = arrayOf(
    "Понедельник",
    "Вторник",
    "Среда",
    "Четверг",
    "Пятница",
    "Суббота"
)

fun getByNumber(number: Int) = days[number - 1]

class ScheduleBuilder(private val group: String) {

    private val scheduleBlock = ScheduleBlock()

    fun build(block: ScheduleBlock.() -> Unit): Schedule {
        block(scheduleBlock)
        return Schedule(group, scheduleBlock.times, scheduleBlock.days)
    }
}

class ScheduleBlock {

    lateinit var times: List<String>

    fun addTimes(block: TimesBlock.() -> Unit) {
        val timesBlock = TimesBlock()
        timesBlock.block()
        times = timesBlock.times
    }

    private var dayCounter = 1
    val days = mutableListOf<Day>()

    fun addDay(day: Int, remote: Boolean = false, block: DayBlock.() -> Unit) {
        val dayBlock = DayBlock()
        dayBlock.block()
        val lessons = dayBlock.blocks

        // Добавляю пустые пары до конца, по размеру расписания
        while (lessons.size < times.size) {
            lessons.add(Block())
        }

        // Добавляю дни с пустыми парами снизу
        val emptyLessonsList = List(times.size) { Block() }
        while (day > dayCounter) {
            days.add(Day(getByNumber(dayCounter), emptyLessonsList))
            dayCounter++
        }

        // Добавляю день с парами
        days.add(Day(getByNumber(dayCounter), dayBlock.blocks, remote))
        dayCounter++
    }

}

class TimesBlock {

    val times = mutableListOf<String>()

    fun add(time: String) {
        times.add(time)
    }
}

class DayBlock {

    private var counter = 1
    val blocks = mutableListOf<Block>()

    fun block(
        number: Int,
        even: Lesson? = null,
        odd: Lesson? = null,
        both: Lesson? = null
    ) {
        // Добавляю пары снизу
        while (number > counter) {
            blocks.add(Block())
            counter++
        }
        // Добавляю пару Number
        blocks.add(Block(even, odd, both))
        counter++
    }
}
