package example.micronaut

import io.micronaut.runtime.Micronaut

class Application {

    companion object {
        fun main(args: Array<String>) {
            Micronaut.run(Application::class.java)
        }
    }

}