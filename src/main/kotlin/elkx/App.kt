package elkx

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import io.vertx.core.Future
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.core.http.impl.MimeMapping
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.eclipse.elk.graph.json.JsonImportException

object ConfigKey {
    const val PORT = "PORT"
}

object Routes {
    const val JSON = "/json"
}

data class LayoutBody(
    val root: JsonObject,
    val opts: JsonObject,
)

private fun layoutSync(ctx: RoutingContext): Future<Void> {

    val bodyStr = ctx.bodyAsString ?: return ctx.response().setStatusCode(400).end()

    val layoutBody = try {
        gsonParse<LayoutBody>(bodyStr)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        return ctx.response().setStatusCode(400).end()
    }

    try {
        layout(layoutBody.root, layoutBody.opts)
    } catch (e: JsonImportException) {
        e.printStackTrace()
        return ctx.response().setStatusCode(400).end()
    }

    return ctx.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, MimeMapping.getMimeTypeForExtension("json"))
        .end(gsonStringify(layoutBody.root))
}

class App : CoroutineVerticle() {

    private lateinit var server: HttpServer

    override suspend fun start() {

        val host = "127.0.0.1"
        val port = config.getInteger(ConfigKey.PORT)
            ?: System.getenv(ConfigKey.PORT)?.toIntOrNull()
            ?: 8080

        val router = Router.router(vertx).apply {
            route()
                .handler(LoggerHandler.create(LoggerFormat.TINY))

            post(Routes.JSON)
                .handler(BodyHandler.create(false))
                .handler(::layoutSync)
        }

        server = vertx.createHttpServer()
            .requestHandler(router)
            .listen(port, host)
            .await()

        println("http://$host:$port")
    }

    override suspend fun stop() {
        server.close().await()
    }
}
