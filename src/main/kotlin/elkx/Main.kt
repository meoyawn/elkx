package elkx

import io.vertx.core.Vertx

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        Vertx.vertx().deployVerticle(App())
    }
}
