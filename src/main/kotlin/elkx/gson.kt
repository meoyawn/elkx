package elkx

import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * ELK uses GSON
 */
private val gson = Gson()

fun gsonParse(s: String): JsonObject =
    gson.fromJson(s, JsonObject::class.java)

fun <T> gsonStringify(x: T): String =
    gson.toJson(x)
