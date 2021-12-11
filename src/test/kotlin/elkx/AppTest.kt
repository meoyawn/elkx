package elkx

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
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

        val deploy = vertx.deployVerticle(
            App(),
            DeploymentOptions().setConfig(JsonObject(mapOf(ConfigKey.PORT to port))),
        )!!

        val client = WebClient.create(vertx)!!

        @JvmStatic
        @BeforeAll
        fun setUp(): Unit = runBlocking {
            deploy.await()
        }

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit = runBlocking {
            vertx.undeploy(deploy.await()).await()
        }
    }

    @Test
    fun hit(): Unit = runBlocking {
        val r = client.post(port, "localhost", Routes.JSON)
            .sendBuffer(Buffer.buffer(EX1))
            .await()

        assertEquals(expected = 200, actual = r.statusCode())

        val root = JsonImporter().transform(gsonParse(r.bodyAsString()))
        val node = root.children.first()
        assertTrue(node.x > 0)
        assertTrue(node.y > 0)
        assertTrue(node.width > 0)
        assertTrue(node.height > 0)
    }
}
