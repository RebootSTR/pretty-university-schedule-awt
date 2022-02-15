package drawer

/**
 *
 * @author Aydar Rafikov
 */
class Day(val dayName: String, val lessons: List<Block>, val remote: Boolean = false) {

    fun isEmpty() = lessons.none { it.isEvenOrOdd() || it.both != null }
}