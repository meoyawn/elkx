package elkx

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AppTest {

    private companion object {
        val vertx = Vertx.vertx()!!

        val port = (9000..10000).random()

        val deployment = vertx.deployVerticle(
            App(),
            DeploymentOptions()
                .setConfig(JsonObject(mapOf(ConfigKey.PORT to port))),
        )!!

        val client = WebClient.create(
            vertx,
            WebClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(port),
        )!!

        @JvmStatic
        @BeforeAll
        fun setUp(): Unit = runBlocking { deployment.await() }

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit = runBlocking { vertx.undeploy(deployment.await()).await() }
    }

    @Test
    fun `no req body`(): Unit = runBlocking {
        val r = client.post(Routes.JSON).send().await()
        assertEquals(expected = 400, actual = r.statusCode())
    }

    @Test
    fun `not json`(): Unit = runBlocking {
        val r = client.post(Routes.JSON).sendBuffer(Buffer.buffer("foo")).await()
        assertEquals(expected = 400, actual = r.statusCode())
    }

    @Test
    fun `empty json`(): Unit = runBlocking {
        val empty = Req(com.google.gson.JsonObject(), com.google.gson.JsonObject())
        val r = client.post(Routes.JSON).sendGson(empty).await()
        assertEquals(expected = 400, actual = r.statusCode())
    }

    @Test
    fun realExample(): Unit = runBlocking {
        val opts = """
{
  "org.eclipse.elk.edgeRouting": "POLYLINE",
  "org.eclipse.elk.direction": "RIGHT",
  "org.eclipse.elk.edgeLabels.inline": "true",
  "org.eclipse.elk.layered.nodePlacement.strategy": "BRANDES_KOEPF",
  "org.eclipse.elk.layered.cycleBreaking.strategy": "GREEDY",
  "org.eclipse.elk.layered.layering.strategy": "NETWORK_SIMPLEX",
  "org.eclipse.elk.layered.crossingMinimization.strategy": "LAYER_SWEEP",
  "org.eclipse.elk.spacing.labelLabel": "18",
  "org.eclipse.elk.layered.interactiveReferencePoint": "TOP_LEFT",
  "org.eclipse.elk.layered.unnecessaryBendpoints": "false",
  "org.eclipse.elk.hierarchyHandling": "INCLUDE_CHILDREN"
}
"""
        val req = Req(gsonParse(resTxt(name = "serverless.json")), gsonParse(opts))
        val r = client.post(Routes.JSON).sendGson(req).await()
        assertEquals(expected = 200, actual = r.statusCode())

        val str = r.bodyAsString()
        val root = toNode(gsonParse(str))
        assertEquals(expected = 339.0, actual = root.height)

        val firstChild = root.children.first()
        assertEquals(expected = 12.0, actual = firstChild.y)
    }
}
