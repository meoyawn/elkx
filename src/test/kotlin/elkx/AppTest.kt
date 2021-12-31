package elkx

import com.google.gson.JsonObject
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.eclipse.elk.graph.json.JsonImporter
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AppTest {

    private companion object {
        val vertx = Vertx.vertx()!!

        val port = (9000..10000).random()

        val client = WebClient.create(
            vertx,
            WebClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(port),
        )!!

        suspend fun layoutRPC(b: LayoutBody): HttpResponse<String> =
            client.post(Routes.JSON)
                .`as`(BodyCodec.string())
                .sendGson(b)
                .await()

        @JvmStatic
        @BeforeAll
        fun setUp(): Unit = runBlocking {
            val options = DeploymentOptions()
                .setConfig(io.vertx.core.json.JsonObject(mapOf(ConfigKey.PORT to port)))
            vertx.deployVerticle(App(), options).await()
        }

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit = runBlocking {
            vertx.close().await()
        }
    }

    @Test
    fun `no req body`(): Unit = runBlocking {
        val resp = client.post(Routes.JSON).send().await()
        assertEquals(expected = 400, actual = resp.statusCode())
    }

    @Test
    fun `not json`(): Unit = runBlocking {
        val resp = client.post(Routes.JSON).sendBuffer(Buffer.buffer("foo")).await()
        assertEquals(expected = 400, actual = resp.statusCode())
    }

    @Test
    fun `empty json`(): Unit = runBlocking {
        val resp = layoutRPC(LayoutBody(root = JsonObject(), opts = JsonObject()))
        assertEquals(expected = 400, actual = resp.statusCode())
    }

    @Test
    fun `layout values`(): Unit = runBlocking {
        val r =
            layoutRPC(LayoutBody(root = gsonParse(resTxt("serverless.json")), opts = gsonParse(resTxt("opts.json"))))
        assertEquals(expected = 200, actual = r.statusCode())

        val root = JsonImporter().transform(gsonParse<JsonObject>(r.body()))
        assertEquals(expected = 339.0, actual = root.height)

        val firstChild = root.children.first()
        assertEquals(expected = 12.0, actual = firstChild.y)
    }

    @Test
    fun concurrency(): Unit = runBlocking {
        val opts = gsonParse<JsonObject>(resTxt("opts.json"))
        val bodies = listOf("analysis.json", "ptolemy.json", "serverless.json")
            .map { LayoutBody(gsonParse(resTxt(it)), opts) }

        (1..100).map {
            launch(Dispatchers.IO) {
                val r = layoutRPC(bodies.random())
                assertEquals(expected = 200, actual = r.statusCode())
            }
        }.joinAll()
    }
}
