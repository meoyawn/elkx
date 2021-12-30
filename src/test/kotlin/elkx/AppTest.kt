package elkx

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.eclipse.elk.graph.json.JsonImporter
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

        @JvmStatic
        @BeforeAll
        fun setUp(): Unit = runBlocking {
            val options = DeploymentOptions()
                .setConfig(JsonObject(mapOf(ConfigKey.PORT to port)))
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
        val resp = client.post(Routes.JSON).sendGson(
            LayoutBody(
                root = com.google.gson.JsonObject(),
                opts = com.google.gson.JsonObject(),
            )
        ).await()
        assertEquals(expected = 400, actual = resp.statusCode())
    }

    @Test
    fun `layout values`(): Unit = runBlocking {
        val body = LayoutBody(gsonParse(resTxt("serverless.json")), gsonParse(resTxt("opts.json")))
        val resp = client.post(Routes.JSON).sendGson(body).await()
        assertEquals(expected = 200, actual = resp.statusCode())

        val str = resp.bodyAsString()
        val root = JsonImporter().transform(gsonParse<com.google.gson.JsonObject>(str))
        assertEquals(expected = 339.0, actual = root.height)

        val firstChild = root.children.first()
        assertEquals(expected = 12.0, actual = firstChild.y)
    }

    @Test
    fun concurrency(): Unit = runBlocking {
        val opts = gsonParse<com.google.gson.JsonObject>(resTxt("opts.json"))
        val bodies = listOf("analysis.json", "ptolemy.json", "serverless.json")
            .map { LayoutBody(gsonParse(resTxt(it)), opts) }

        val codes = (1..100).map {
            async(Dispatchers.Default) {
                client.post(Routes.JSON).sendGson(bodies.random()).await().statusCode()
            }
        }.awaitAll()

        assertTrue(codes.all { it == 200 })
    }
}
