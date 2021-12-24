package elkx

import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.impl.MimeMapping
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
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

fun <T> HttpRequest<Buffer>.sendGson(x: T): Future<HttpResponse<Buffer>> =
    putHeader(HttpHeaders.CONTENT_TYPE.toString(), MimeMapping.getMimeTypeForExtension("json"))
        .sendBuffer(Buffer.buffer(gsonStringify(x)))
