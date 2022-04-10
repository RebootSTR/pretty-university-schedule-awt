package rafikov.prettyuniversityschedule.drawer.implementation

import rafikov.prettyuniversityschedule.drawer.abstracts.CoreDrawer
import rafikov.prettyuniversityschedule.models.domain.Schedule
import rafikov.prettyuniversityschedule.paint.abstracts.AbstractPaint
import rafikov.prettyuniversityschedule.paint.implementation.AwtPaint
import java.awt.Color

/**
 *
 * @author Aydar Rafikov
 */
open class AwtDrawer(schedule: Schedule) : CoreDrawer<Color, Int>(schedule) {
    override val blackColor: Color = Color.BLACK
    override val copyrightColor: Color = Color(191, 191, 191)
    override val copyrightSize: Int = 100
    override val dayFontSize: Int = 100
    override val emptyColor: Color = Color(217, 217, 217)
    override val labColor: Color = Color(255, 0, 0)
    override val lectureColor: Color = Color.BLACK
    override val lessonFontSize: Int = 44
    override val paint: AbstractPaint<Color, Int> = AwtPaint()
    override val practiceColor: Color = Color(0, 112, 192)
    override val remoteColor: Color = Color(235, 247, 255)
    override val teacherColor: Color = Color(0, 0, 0, 150)
    override val teacherFontSize: Int = 30
    override val timeFontSize: Int = 50
    override val whiteColor: Color = Color.WHITE
}