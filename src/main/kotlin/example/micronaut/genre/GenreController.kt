package example.micronaut.genre

import example.micronaut.ApplicationConfiguration
import example.micronaut.PaginationArguments
import example.micronaut.SortingArguments
import example.micronaut.SortingOrder
import example.micronaut.domain.Genre
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.validation.Validated
import javax.validation.Valid
import java.net.URI

@Validated // <1>
@Controller("/genres") // <2>
open class GenreController(genreRepository: GenreRepository,
                      applicationConfiguration: ApplicationConfiguration)// <3>
{

    /*
        the following is non-idiomatic but included to resolve AOP error on generated getters/setters
        with advice from here, https://stackoverflow.com/questions/38492103/override-getter-for-kotlin-data-class
     */
    protected open val genreRepository: GenreRepository = genreRepository
    protected open val applicationConfiguration: ApplicationConfiguration = applicationConfiguration

    @Get("/{id}") // <4>
    open fun show(id: Long?): Genre? {
        return genreRepository
                .findById(id)
                .orElse(null) // <5>
    }

    @Put("/") // <6>
    open fun update(@Body @Valid command: GenreUpdateCommand): HttpResponse<*> { // <7>
        val numberOfEntitiesUpdated = genreRepository.update(command.id, command.name!!)

        return HttpResponse
                .noContent<Any>()
                .header(HttpHeaders.LOCATION, location(command.id).path) // <8>
    }

    @Get(value = "/") // <9>
    open fun list(offset: Int?,
             max: Int?,
             sort: String?,
             order: String?): List<Genre> {
        val paginationArguments = PaginationArguments(offset ?: 0,
                max ?: applicationConfiguration.getMax())
        val sortingArguments = if (sort != null) SortingArguments(sort, SortingOrder.of(order)) else null

        return genreRepository.findAll(paginationArguments, sortingArguments)
    }

    @Post("/") // <10>
    open fun save(@Body @Valid cmd: GenreSaveCommand): HttpResponse<Genre> {
        val genre = genreRepository.save(cmd.name)

        return HttpResponse
                .created(genre)
                .headers { headers -> headers.location(location(genre.id)) }
    }

    @Delete("/{id}") // <11>
    open fun delete(id: Long?): HttpResponse<*> {
        genreRepository.deleteById(id)
        return HttpResponse.noContent<Any>()
    }

    protected open fun location(id: Long?): URI {
        return URI.create("/genres/" + id!!)
    }

    protected open fun location(genre: Genre): URI {
        return location(genre.id)
    }
}
