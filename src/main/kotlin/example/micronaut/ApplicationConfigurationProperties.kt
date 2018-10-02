package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("application")
open class ApplicationConfigurationProperties : ApplicationConfiguration {
    protected val DEFAULT_MAX = 10

    private val max = DEFAULT_MAX

    override fun getMax(): Int {
        return max
    }
}