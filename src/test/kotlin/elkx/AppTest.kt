package elkx

import com.google.gson.JsonObject
import io.grpc.Grpc
import io.grpc.InsecureChannelCredentials
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.eclipse.elk.graph.json.JsonImporter
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Paths
import kotlin.io.path.name
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppTest {

    private companion object {

        val port = (9000..10000).random()

        val server = mkServer(port)

        val chan =
            Grpc.newChannelBuilderForAddress("localhost", port, InsecureChannelCredentials.create())
                .build()!!

        val stub = ElkXGrpcKt.ElkXCoroutineStub(chan)

        @JvmStatic
        @BeforeAll
        fun setUp() {
            server.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            server.shutdownNow()
            chan.shutdownNow()
        }
    }

    @Test
    fun `no req body`(): Unit = runBlocking {
        val e = assertThrows<StatusException> {
            stub.layout(layoutReq { })
        }
        assertEquals(expected = Status.UNKNOWN, actual = e.status)
    }

    @Test
    fun `not json`(): Unit = runBlocking {
        val e = assertThrows<StatusException> {
            stub.layout(layoutReq {
                root = "foo"
            })
        }
        assertEquals(expected = Status.UNKNOWN, actual = e.status)
    }

    @Test
    fun `empty json`(): Unit = runBlocking {
        val e = assertThrows<StatusException> {
            stub.layout(layoutReq {
                root = gsonStringify(JsonObject())
            })
        }
        assertEquals(expected = Status.UNKNOWN, actual = e.status)
    }

    @Test
    fun `layout values`(): Unit = runBlocking {
        val resp = stub.layout(layoutReq {
            root = resTxt(Paths.get("jsons", "serverless.json"))
            opts.putAll(parseMap(resTxt(Paths.get("opts.json"))))
        })

        val str = resp.root
        val root = JsonImporter().transform(gsonParse(str))
        assertEquals(expected = 339.0, actual = root.height)

        val firstChild = root.children.first()
        assertEquals(expected = 12.0, actual = firstChild.y)
    }

    @Test
    fun concurrency(): Unit = runBlocking {
        val optsJson = parseMap(resTxt(Paths.get("opts.json")))
        val bodies = listDir(Paths.get("jsons")).map {
            layoutReq {
                root = resTxt(Paths.get("jsons", it.name))
                opts.putAll(optsJson)
            }
        }

        val codes = (1..100).map {
            async(Dispatchers.IO) {
                stub.layout(bodies.random())
            }
        }.awaitAll()

        assertTrue(codes.all { it.root.isNotEmpty() })
    }
}
