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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.eclipse.elk.graph.json.JsonImportException
import kotlin.coroutines.CoroutineContext

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

    val reqBody = gsonParse<LayoutBody>(bodyStr)
    layout(reqBody.root, reqBody.opts)

    return ctx.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, MimeMapping.getMimeTypeForExtension("json"))
        .end(gsonStringify(reqBody.root))
}

private fun maybeIts400(ctx: RoutingContext) {
    when (ctx.failure()) {
        is JsonSyntaxException, is JsonImportException ->
            ctx.response().setStatusCode(400).end()

        else ->
            ctx.next()
    }
}

private fun CoroutineScope.coroutine(
    req: RoutingContext,
    thread: CoroutineContext = this.coroutineContext,
    work: suspend (req: RoutingContext) -> Unit,
) =
    launch(thread) {
        try {
            work(req)
        } catch (e: Exception) {
            req.fail(e)
        }
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
                .handler { ctx ->
                    coroutine(ctx, Dispatchers.Default) {
                        layoutSync(ctx)
                    }
                }
                .failureHandler(::maybeIts400)
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
