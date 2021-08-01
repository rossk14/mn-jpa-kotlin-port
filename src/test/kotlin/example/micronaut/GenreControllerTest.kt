package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.annotation.Get
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test

import javax.inject.Inject

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import java.util.Collections

@MicronautTest 
public class GenreControllerTest {

    @Inject
    @Client("/")
    lateinit var client: HttpClient

    @Test
    fun testFindNonExistingGenreReturns404() {
        val thrown: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java) {
            val request: HttpRequest<Any> = HttpRequest.GET("/genres/99")
            client.toBlocking().exchange(request, Argument.listOf(Genre::class.java)) 
        }

        assertNotNull(thrown.getResponse())
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus())
    }

    @Test
    fun testGenreCrudOperations() {

        val genreIds: MutableList<Long> = mutableListOf<Long>()

        var request: HttpRequest<Any> = HttpRequest.POST("/genres", Collections.singletonMap("name", "DevOps")) 
        var response: HttpResponse<Any> = client.toBlocking().exchange(request)
        genreIds.add(entityId(response) ?: 0)

        assertEquals(HttpStatus.CREATED, response.getStatus())

        request = HttpRequest.POST("/genres", Collections.singletonMap("name", "Microservices")) 
        response = client.toBlocking().exchange(request)

        assertEquals(HttpStatus.CREATED, response.getStatus())

        var id: Long = entityId(response) ?: 0
        genreIds.add(id)
        request = HttpRequest.GET("/genres/" + id)

        var genre: Genre = client.toBlocking().retrieve(request, Genre::class.java) 

        assertEquals("Microservices", genre.name)

        request = HttpRequest.PUT("/genres", GenreUpdateCommand(id, "Micro-services"))
        response = client.toBlocking().exchange(request)  

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus())

        request = HttpRequest.GET("/genres/" + id)
        genre = client.toBlocking().retrieve(request, Genre::class.java)
        assertEquals("Micro-services", genre.name)

        request = HttpRequest.GET("/genres/list")
        var genres: MutableList<*> = client.toBlocking().retrieve(request, Argument.of(MutableList::class.java, Genre::class.java))

        assertEquals(2, genres.size)

        request = HttpRequest.POST("/genres/ex", Collections.singletonMap("name", "Microservices")) 
        response = client.toBlocking().exchange(request)

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus())

        request = HttpRequest.GET("/genres/list")
        genres = client.toBlocking().retrieve(request, Argument.of(MutableList::class.java, Genre::class.java))

        assertEquals(2, genres.size)

        request = HttpRequest.GET("/genres/list?size=1")
        genres = client.toBlocking().retrieve(request, Argument.of(MutableList::class.java, Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("DevOps", (genres.get(0) as Genre).name)

        request = HttpRequest.GET("/genres/list?size=1&sort=name,desc")
        genres = client.toBlocking().retrieve(request, Argument.of(MutableList::class.java, Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("Micro-services",  (genres.get(0) as Genre).name)

        request = HttpRequest.GET("/genres/list?size=1&page=2")
        genres = client.toBlocking().retrieve(request, Argument.of(MutableList::class.java, Genre::class.java))

        assertEquals(0, genres.size)

        // cleanup:
        for (genreId: Long in genreIds) {
            request = HttpRequest.DELETE("/genres/" + genreId)
            response = client.toBlocking().exchange(request)
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus())
        }
    }

    fun entityId(response: HttpResponse<Any>): Long? {
        val path: String  = "/genres/"
        var value: String = response.header(HttpHeaders.LOCATION)

        var index: Int = value.indexOf(path)
        if (index != -1) {
            return value.substring(index + path.length).toLong()
        }
        return null
    }

}