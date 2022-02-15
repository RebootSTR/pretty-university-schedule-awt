package drawer

/**
 *
 * @author Aydar Rafikov
 */
class Block(
    val even: Lesson? = null,
    val odd: Lesson? = null,
    val both: Lesson? = null
) {

    fun isEvenOrOdd() = even != null || odd != null
}