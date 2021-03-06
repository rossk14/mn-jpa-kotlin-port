package example.micronaut

import io.micronaut.runtime.Micronaut

object IntellijApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.run(Application::class.java)
    }
}