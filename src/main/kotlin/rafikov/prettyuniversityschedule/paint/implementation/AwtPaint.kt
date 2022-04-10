package rafikov.prettyuniversityschedule.paint.implementation

import rafikov.prettyuniversityschedule.models.core.Alignment
import rafikov.prettyuniversityschedule.models.core.Arrangement
import rafikov.prettyuniversityschedule.models.core.Rectangle
import rafikov.prettyuniversityschedule.models.core.Typeface
import rafikov.prettyuniversityschedule.paint.abstracts.AbstractPaint
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 *
 * @author Aydar Rafikov
 */
class AwtPaint : AbstractPaint<Color, Int> {

    private lateinit var img: BufferedImage
    private lateinit var graphics: Graphics2D

    private var currentAlignment = Alignment.CENTER
    private var currentArrangement = Arrangement.CENTER

    override fun cropImageToHeight(height: Int) {
        img = img.getSubimage(0, 0, img.width, height)
        graphics = img.graphics as Graphics2D
    }

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        graphics.drawLine(x1, y1, x2, y2)
    }

    override fun getVerticalOffsetForNewLine(): Int {
        val metrics = graphics.fontMetrics
        return (metrics.height * 0.7).toInt()
    }

    override fun drawText(text: String, rect: Rectangle) {
        val metrics = graphics.fontMetrics
        when {
            // center
            (currentAlignment == Alignment.CENTER && currentArrangement == Arrangement.CENTER) -> {
                val x: Int = rect.left + (rect.width - metrics.stringWidth(text)) / 2
                val y: Int = rect.top + (rect.height - metrics.height) / 2 + metrics.ascent + 7
                graphics.drawString(text, x, y)
            }
            // top right corner
            (currentAlignment == Alignment.RIGHT && currentArrangement == Arrangement.TOP) -> {
                val x: Int = rect.left + (rect.width - metrics.stringWidth(text)) - 5
                val y: Int = rect.top + metrics.ascent + 5
                graphics.drawString(text, x, y)
            }
            else -> {
                throw NotImplementedError("Text direction alignment ${currentAlignment.name} and arrangement ${currentArrangement.name} not implemented")
            }
        }

    }

    override fun drawVerticalText(text: String, rect: Rectangle) {
        val metrics = graphics.fontMetrics
        val x: Int = rect.left + (rect.height - metrics.stringWidth(text)) / 2
        val y: Int = rect.top + (rect.width - metrics.height) / 2 + metrics.ascent + 14

        rotateFont(-Math.PI / 2)
        graphics.drawString(text, y - rect.top + rect.left, rect.top + rect.height - x + rect.left)
        rotateFont(0.0)
    }

    private fun rotateFont(degree: Double) {
        val rotate = AffineTransform().apply {
            rotate(degree)
        }
        graphics.font = graphics.font.deriveFont(rotate)
    }

    override fun fillRect(rect: Rectangle) {
        graphics.fillRect(rect.left, rect.top, rect.width, rect.height)
    }

    override fun getColor(): Color {
        return graphics.color
    }

    override fun getTextSize(): Int {
        return graphics.font.size
    }

    override fun prepareToDraw(width: Int, height: Int, font: String) {
        val desktopHints = Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints") as Map<*, *>
        img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        graphics = img.createGraphics() as Graphics2D
        graphics.setRenderingHints(desktopHints)
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.font = Font(font, Font.PLAIN, 1)
    }

    override fun save(file: File) {
        ImageIO.write(img, "png", file.also {
            it.createNewFile()
        })
    }

    override fun setAlignment(alignment: Alignment) {
        currentAlignment = alignment
    }

    override fun setArrangement(arrangement: Arrangement) {
        currentArrangement = arrangement
    }

    override fun setBorderStroke(stroke: Int) {
        graphics.stroke = BasicStroke(stroke.toFloat())
    }

    override fun setColor(color: Color) {
        graphics.color = color
    }

    override fun setTextSize(textSize: Int) {
        graphics.font = graphics.font.deriveFont(textSize.toFloat())
    }

    override fun setTypeface(typeface: Typeface) {
        when (typeface) {
            Typeface.BOLD -> {
                graphics.font = graphics.font.deriveFont(Font.BOLD)
            }
            Typeface.NORMAL -> {
                graphics.font = graphics.font.deriveFont(Font.PLAIN)
            }
            else -> {
                throw NotImplementedError("typeface ${typeface.name} unknown")
            }
        }
    }
}