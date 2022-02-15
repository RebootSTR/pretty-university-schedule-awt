package json

import com.google.gson.annotations.SerializedName

/**
 *
 * @author Aydar Rafikov
 */
class Schedule(
    @SerializedName("group_name") val groupName: String,
    val times: List<String>,
    val days: List<Day>
) {
}