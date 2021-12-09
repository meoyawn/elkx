package elkx

import io.vertx.config.ConfigRetriever
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.core.http.impl.MimeMapping
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

private inline fun <T> ConfigRetriever.use(f: (ConfigRetriever) -> T): T {
    try {
        return f(this)
    } finally {
        close()
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
                        val inp = ctx.bodyAsJson
                        ctx.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, MimeMapping.getMimeTypeForExtension("json"))
                            .end(JsonObject().toBuffer())
                    }
            })
            .listen(port, "127.0.0.1")
            .await()
    }

    override suspend fun stop() {
        server.close().await()
    }
}
