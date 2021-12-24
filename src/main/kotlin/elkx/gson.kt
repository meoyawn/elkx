package elkx

import com.google.gson.Gson

/**
 * ELK uses GSON
 */
val gson = Gson()

inline fun <reified T> gsonParse(s: String): T =
    gson.fromJson(s, T::class.java)

fun <T> gsonStringify(x: T): String =
    gson.toJson(x)
