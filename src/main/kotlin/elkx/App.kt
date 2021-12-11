package elkx

import io.vertx.config.ConfigRetriever
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.core.http.impl.MimeMapping
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private inline fun <T> ConfigRetriever.use(f: (ConfigRetriever) -> T): T {
    try {
        return f(this)
    } finally {
        close()
    }
}

private inline fun CoroutineScope.coroutine(ctx: RoutingContext, crossinline fn: suspend () -> Unit): Job =
    launch {
        try {
            fn()
        } catch (e: Exception) {
            ctx.fail(e)
        }
    }

object ConfigKey {
    const val PORT = "http.port"
}

object Routes {
    const val JSON = "/json"
}

class App : CoroutineVerticle() {

    private lateinit var server: HttpServer

    override suspend fun start() {

        val cfg = ConfigRetriever.create(vertx).use { it.config.await() }.mergeIn(config)
        val port = cfg.getInteger(ConfigKey.PORT)

        server = vertx.createHttpServer()
            .requestHandler(Router.router(vertx).apply {
                post(Routes.JSON)
                    .handler(BodyHandler.create(false))
                    .handler { ctx ->
                        coroutine(ctx) {
                            val gsonObj = withContext(Dispatchers.Default) {
                                gsonParse(ctx.bodyAsString).also(::layout)
                            }
                            ctx.response()
                                .putHeader(HttpHeaders.CONTENT_TYPE, MimeMapping.getMimeTypeForExtension("json"))
                                .end(gsonStringify(gsonObj))
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
