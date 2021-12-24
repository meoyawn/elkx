package elkx

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.eclipse.elk.graph.json.JsonImporter
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import kotlin.io.path.readText
import kotlin.test.assertEquals

class AppTest {

    private companion object {
        val vertx = Vertx.vertx()!!

        val port = (9000..10000).random()

        val deployment = vertx.deployVerticle(
            App(),
            DeploymentOptions().setConfig(JsonObject(mapOf(ConfigKey.PORT to port))),
        )!!

        val client = WebClient.create(vertx)!!

        @JvmStatic
        @BeforeAll
        fun setUp(): Unit = runBlocking {
            deployment.await()
        }

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit = runBlocking {
            vertx.undeploy(deployment.await()).await()
        }

        suspend fun resourceText(name: String): String =
            withContext(Dispatchers.IO) {
                val res = javaClass.classLoader.getResource(name) ?: throw NoSuchFileException(name)
                Paths.get(res.toURI()).readText()
            }
    }

    @Test
    fun realExample(): Unit = runBlocking {
        val r = client.post(port, "localhost", Routes.JSON)
            .sendBuffer(Buffer.buffer(resourceText("serverless.json")))
            .await()
        assertEquals(expected = 200, actual = r.statusCode())

        val root = JsonImporter().transform(gsonParse(r.bodyAsString()))
        assertEquals(expected = 339.0, actual = root.height)

        val firstChild = root.children.first()
        assertEquals(expected = 5.0, actual = firstChild.y)
    }
}
