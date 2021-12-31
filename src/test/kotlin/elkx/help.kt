package elkx

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText

suspend fun resTxt(name: Path): String =
    withContext(Dispatchers.IO) {
        val res = javaClass.classLoader.getResource(name.toString()) ?: throw NoSuchFileException(name.toString())
        Paths.get(res.toURI()).readText()
    }

suspend fun listDir(name: Path): List<Path> =
    withContext(Dispatchers.IO) {
        val res = javaClass.classLoader.getResource(name.toString()) ?: throw NoSuchFileException(name.toString())
        Paths.get(res.toURI()).listDirectoryEntries()
    }
