package elkx

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import kotlin.io.path.readText

suspend fun resTxt(name: String): String =
    withContext(Dispatchers.IO) {
        val res = javaClass.classLoader.getResource(name) ?: throw NoSuchFileException(name)
        Paths.get(res.toURI()).readText()
    }
