package elkx

import com.google.gson.Gson
import com.google.gson.JsonObject

private val gson = Gson()

fun gsonParse(s: String): JsonObject =
    gson.fromJson(s, JsonObject::class.java)

fun gsonStringify(x: JsonObject): String =
    gson.toJson(x)
