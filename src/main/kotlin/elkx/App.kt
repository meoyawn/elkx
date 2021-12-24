package elkx

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import io.vertx.config.ConfigRetriever
import io.vertx.core.Future
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.core.http.impl.MimeMapping
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.elk.graph.json.JsonImportException

private inline fun <T> ConfigRetriever.use(f: (ConfigRetriever) -> T): T =
    try {
        f(this)
    } finally {
        close()
    }

object ConfigKey {
    const val PORT = "http.port"
}

object Routes {
    const val JSON = "/json"
}

data class LayoutBody(
    val root: JsonObject,
    val opts: JsonObject,
)

private fun layoutEndpoint(ctx: RoutingContext): Future<Void> {

    val bodyStr = ctx.bodyAsString ?: return ctx.response().setStatusCode(400).end()

    val layoutBody = try {
        gsonParse<LayoutBody>(bodyStr)
    } catch (e: JsonSyntaxException) {
        return ctx.response().setStatusCode(400).end()
    }

    try {
        layoutEndpoint(layoutBody.root, layoutBody.opts)
    } catch (e: JsonImportException) {
        return ctx.response().setStatusCode(400).end()
    }

    val resp = gsonStringify(layoutBody.root)

    return ctx.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, MimeMapping.getMimeTypeForExtension("json"))
        .end(resp)
}

class App : CoroutineVerticle() {

    private lateinit var server: HttpServer

    private fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
        handler { ctx ->
            launch {
                try {
                    fn(ctx)
                } catch (e: Exception) {
                    ctx.fail(e)
                }
            }
        }
    }

    override suspend fun start() {

        val cfg = ConfigRetriever.create(vertx).use { it.config.await() }.mergeIn(config)
        val port = cfg.getInteger(ConfigKey.PORT)

        server = vertx.createHttpServer()
            .requestHandler(Router.router(vertx).apply {
                post(Routes.JSON)
                    .handler(BodyHandler.create(false))
                    .coroutineHandler { ctx ->
                        withContext(Dispatchers.Default) {
                            layoutEndpoint(ctx)
                        }
                    }
            })
            .listen(port, "127.0.0.1")
            .await()
    }

    override suspend fun stop() {
        server.close().await()
    }
}
