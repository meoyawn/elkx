package elkx

import io.grpc.Grpc
import io.grpc.InsecureServerCredentials
import io.grpc.Server
import kotlinx.coroutines.Dispatchers

private class ElkService : ElkXGrpcKt.ElkXCoroutineImplBase(Dispatchers.Default) {

    override suspend fun layout(request: Elkx.LayoutReq): Elkx.LayoutResp {
        val obj = gsonParse(request.root)

        layout(obj, gsonParse(request.opts))

        return layoutResp { root = gsonStringify(obj) }
    }
}

fun mkServer(port: Int): Server =
    Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
        .addService(ElkService())
        .build()

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val port = System.getenv("PORT") ?: "8080"
        val server = mkServer(port.toInt())
        server.start()
        println("http://localhost:8080")
        server.awaitTermination()
    }
}
