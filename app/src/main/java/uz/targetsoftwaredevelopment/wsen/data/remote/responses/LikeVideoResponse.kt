package uz.targetsoftwaredevelopment.wsen.data.remote.responses

import com.google.gson.annotations.SerializedName

data class LikeVideoResponse(
	val data: LikeVideResponseData? = null
)

data class LikeVideResponseData(
	val owner: Int? = null,
	val preload_img: String? = null,
	val like: Int? = null,
	val dislike: Int? = null,
	val created_at: String? = null,
	val video: String? = null,
	val title: String? = null,
	val chat_link: Any? = null,
	val is_liked_by_currentUser:Boolean = false,
	@field:SerializedName("updated_at")
	val updayed_at: String? = null,
	val location: String? = null,
	val id: Int? = null,
	val category: Int? = null,
	val status: String? = null,
	val desc: String? = null
)
