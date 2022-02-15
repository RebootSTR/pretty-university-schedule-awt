package drawer

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 *
 * @author Aydar Rafikov
 */
const val CELL = 20
const val WIDTH = 79 * CELL
const val SMALL_BORDER = 2
const val BIG_BORDER = 5
const val VOID_BORDER = 1 * CELL
const val HEADER_HEIGHT = 4 * CELL
const val LESSON_LINE_HEIGHT: Int = (5.45 * CELL).toInt()
const val GROUP_NAME_WIDTH = (21.95 * CELL).toInt()
const val COPYRIGHT = "@rebootstr"
const val COPYRIGHT_SIZE = 100f
const val DAY_NAME_WIDTH = 6 * CELL
const val DAY_FONT_SIZE = 100
const val TIME_FONT_SIZE = 50
const val LESSON_FONT_SIZE = 44
const val TEACHER_FONT_SIZE = 30
const val FONT = "Calibri"
const val COUNT_LESSONS_IN_EMPTY_DAY = 3

const val DRAW_GRID = false

val remoteColor = Color(235, 247, 255)
val emptyColor = Color(217, 217, 217)

val practiceColor = Color(0, 112, 192)
val labColor = Color(255, 0, 0)
val lectureColor: Color = Color.BLACK
val teacherColor: Color = Color(0, 0, 0, 150)
val copyrightColor: Color = Color(191, 191, 191)

class ScheduleDrawer(private val schedule: Schedule) {

    private var img: BufferedImage
    private var graphics: Graphics2D
    private var height: Int

    init {
        val desktopHints = Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints") as Map<*, *>
        height = VOID_BORDER * 2 + HEADER_HEIGHT + LESSON_LINE_HEIGHT * schedule.times.size * schedule.days.size
        img = BufferedImage(WIDTH, height, BufferedImage.TYPE_INT_RGB)
        graphics = img.createGraphics() as Graphics2D
        graphics.setRenderingHints(desktopHints)
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.color = Color.WHITE
        drawCell(0, 0, WIDTH, height)
        initInstruments()
    }

    private fun initInstruments() {
        graphics.color = Color.BLACK
        graphics.font = Font(FONT, Font.PLAIN, TIME_FONT_SIZE)
    }

    fun drawSchedule() {
        val width = WIDTH - VOID_BORDER * 2

        val headerX = VOID_BORDER
        val headerY = VOID_BORDER
        drawHeaders(headerX, headerY, HEADER_HEIGHT, width)

        val dayX = headerX
        val dayY = headerY + HEADER_HEIGHT
        var lastLessonCount = 0
        for (day in schedule.days.withIndex()) {
            if (day.value.isEmpty()) {
                drawEmptyDay(dayX, dayY + lastLessonCount * LESSON_LINE_HEIGHT, LESSON_LINE_HEIGHT, width, day.value)
                lastLessonCount += COUNT_LESSONS_IN_EMPTY_DAY
            } else {
                drawDay(dayX, dayY + lastLessonCount * LESSON_LINE_HEIGHT, LESSON_LINE_HEIGHT, width, day.value)
                lastLessonCount += day.value.lessons.size
            }
        }

        // crop
        img = img.getSubimage(0, 0, img.width, dayY + lastLessonCount * LESSON_LINE_HEIGHT + VOID_BORDER)
        graphics = img.graphics as Graphics2D
        initInstruments()
    }

    private fun drawHeaders(x: Int, y: Int, height: Int, width: Int) {
        val evenAndOddWidth = (width - GROUP_NAME_WIDTH) / 2

        drawBoldString(schedule.group, Rectangle(x, y, GROUP_NAME_WIDTH, height), BIG_BORDER)
        drawString("ЧЕТ", Rectangle(x + GROUP_NAME_WIDTH, y, evenAndOddWidth, height), BIG_BORDER)
        drawString("НЕЧ", Rectangle(x + GROUP_NAME_WIDTH + evenAndOddWidth, y, evenAndOddWidth, height), BIG_BORDER)
    }

    private fun drawEmptyDay(x: Int, y: Int, lineHeight: Int, width: Int, day: Day) {

        // background
        withColor(emptyColor) {
            drawCell(x, y, width, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY)
        }

        // global border
        drawBorder(Rectangle(x, y, width, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY), BIG_BORDER)

        // day name
        drawBoldString(
            day.dayName,
            Rectangle(x, y, DAY_NAME_WIDTH, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY),
            BIG_BORDER,
            true,
            size = DAY_FONT_SIZE
        )

        // times
        val timeWidth = GROUP_NAME_WIDTH - DAY_NAME_WIDTH
        val timeX = x + DAY_NAME_WIDTH
        drawBorder(Rectangle(timeX, y, timeWidth, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY), BIG_BORDER)

        // copyright
        withSize(COPYRIGHT_SIZE) {
            drawString(
                COPYRIGHT,
                Rectangle(timeX + timeWidth, y + lineHeight, (width - DAY_NAME_WIDTH - timeWidth), lineHeight),
                color = copyrightColor
            )
        }
    }

    private fun drawDay(x: Int, y: Int, lineHeight: Int, width: Int, day: Day) {

        // background
        if (day.remote) {
            withColor(remoteColor) {
                drawCell(x, y, width, lineHeight * schedule.times.size)
            }
        }

        // global border
        drawBorder(Rectangle(x, y, width, lineHeight * schedule.times.size), BIG_BORDER)

        // day name
        drawBoldString(
            day.dayName,
            Rectangle(x, y, DAY_NAME_WIDTH, lineHeight * schedule.times.size),
            BIG_BORDER,
            true,
            size = DAY_FONT_SIZE
        )

        // times
        val timeWidth = GROUP_NAME_WIDTH - DAY_NAME_WIDTH
        val timeX = x + DAY_NAME_WIDTH
        for (i in schedule.times.indices) {
            drawString(schedule.times[i], Rectangle(timeX, y + i * lineHeight, timeWidth, lineHeight))
        }
        drawBorder(Rectangle(timeX, y, timeWidth, lineHeight * schedule.times.size), BIG_BORDER)

        // lessons
        val lessonWidth = (width - DAY_NAME_WIDTH - timeWidth) / 2
        val lesson1X = timeX + timeWidth
        val lesson2X = lesson1X + lessonWidth
        for (les in day.lessons.withIndex()) {
            val lY = y + les.index * lineHeight
            val fullRect = Rectangle(lesson1X, lY, lessonWidth * 2, lineHeight)
            val lesson = les.value
            when {
                lesson.isEvenOrOdd() -> {
                    drawBorder(fullRect, SMALL_BORDER)
                    lesson.even?.run {
                        drawLesson(this, Rectangle(lesson1X, lY, lessonWidth, lineHeight))
                    }
                    lesson.odd?.run {
                        drawLesson(this, Rectangle(lesson2X, lY, lessonWidth, lineHeight))
                    }
                }
                lesson.both != null -> {
                    drawLesson(lesson.both, fullRect)
                }
            }
        }
    }

    private fun drawLesson(lesson: Lesson, rect: Rectangle) {
        drawBorder(rect, SMALL_BORDER)
        val strings = lesson.teacher.split(" ".toRegex(), 2)
        drawStringInCorner(strings[0], rect, color = teacherColor, size = TEACHER_FONT_SIZE)
        drawStringInCorner(
            strings[1],
            rect.also { it.y += TEACHER_FONT_SIZE },
            color = teacherColor,
            size = TEACHER_FONT_SIZE
        )
    }

    private fun rotateFont(degree: Double) {
        val rotate = AffineTransform().apply {
            rotate(degree)
        }
        graphics.font = graphics.font.deriveFont(rotate)
    }

    private fun drawGrid() {
        val gridCount = WIDTH / CELL

        for (i in 1..gridCount) {
            graphics.drawLine(CELL * i, 0, CELL * i, height)
            graphics.drawLine(0, CELL * i, WIDTH, CELL * i)
        }
    }

    private fun drawCell(x: Int, y: Int, width: Int, height: Int) {
        graphics.fillRect(x, y, width, height)
    }

    private fun withColor(color: Color, action: () -> Unit) {
        graphics.color = color
        action()
        graphics.color = Color.BLACK
    }

    private fun withSize(size: Float, action: () -> Unit) {
        val font = graphics.font
        graphics.font = font.deriveFont(size)
        action()
        graphics.font = font
    }

    private fun drawString(
        text: String,
        rect: Rectangle,
        border: Int = 0,
        rotated: Boolean = false,
        color: Color = Color.BLACK,
        size: Int = graphics.font.size
    ) {
        if (rotated) {
            drawRotatedTextWithBorder(text, rect, border, color)
        }
        if (border != 0) {
            drawBorder(rect, border)
        }
        withSize(size.toFloat()) {
            withColor(color) {
                val metrics = graphics.fontMetrics
                val x: Int = rect.x + (rect.width - metrics.stringWidth(text)) / 2
                val y: Int = rect.y + (rect.height - metrics.height) / 2 + metrics.ascent + 14
                graphics.drawString(text, x, y)
            }
        }
    }

    private fun drawStringInCorner(
        text: String,
        rect: Rectangle,
        color: Color = Color.BLACK,
        size: Int = graphics.font.size
    ) {
        withSize(size.toFloat()) {
            withColor(color) {
                val metrics = graphics.fontMetrics
                val x: Int = rect.x + (rect.width - metrics.stringWidth(text)) - 10
                val y: Int = rect.y + metrics.ascent + 10
                graphics.drawString(text, x, y)
            }
        }
    }

    private fun drawBoldString(
        text: String,
        rect: Rectangle,
        border: Int = 0,
        rotated: Boolean = false,
        color: Color = Color.BLACK,
        size: Int = graphics.font.size
    ) {
        val font = graphics.font
        graphics.font = font.deriveFont(Font.BOLD)
        drawString(text, rect, border, rotated, color, size)
        graphics.font = font
    }

    private fun drawRotatedTextWithBorder(
        text: String,
        rect: Rectangle,
        border: Int,
        color: Color = Color.BLACK,
        size: Int = graphics.font.size
    ) {
        drawBorder(rect, border)

        withSize(size.toFloat()) {
            withColor(color) {
                val metrics = graphics.fontMetrics
                val x: Int = rect.x + (rect.height - metrics.stringWidth(text)) / 2
                val y: Int = rect.y + (rect.width - metrics.height) / 2 + metrics.ascent + 14

                rotateFont(-Math.PI / 2)
                graphics.drawString(text, y - rect.y + rect.x, rect.y + rect.height - x + rect.x)
            }
        }
        rotateFont(0.0)
    }

    private fun drawBorder(rect: Rectangle, border: Int) {
        graphics.stroke = BasicStroke(border.toFloat())
        graphics.drawRect(rect.x, rect.y, rect.width, rect.height)
    }

    fun save(fileName: String) {
        if (DRAW_GRID) {
            drawGrid()
        }
        ImageIO.write(img, "png", File("$fileName.png").also {
            it.createNewFile()
        })
    }

    private fun Lesson.getColor(): Color = when (this.type) {
        LessonType.PRACTICE -> {
            practiceColor
        }
        LessonType.LAB -> {
            labColor
        }
        LessonType.LECTURE -> {
            lectureColor
        }
    }
}