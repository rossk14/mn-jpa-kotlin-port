package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.http.HttpStatus
import javax.validation.constraints.NotBlank
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.data.exceptions.DataAccessException
import javax.validation.Valid
import java.net.URI
import java.util.Optional
import java.util.stream.Collectors
import java.util.stream.StreamSupport

@ExecuteOn(TaskExecutors.IO)  
@Controller("/genres")  
open class GenreController(val genreRepository: GenreRepository) {


    @Get("/{id}") 
    open fun show(id: Long): Optional<Genre> {
        return genreRepository
            .findById(id); 
    }

    @Put 
    open fun update(@Body @Valid command: GenreUpdateCommand): HttpResponse<HttpStatus> { 
        genreRepository.update(Genre(command.id, command.name))
        return HttpResponse
                .noContent<HttpStatus>()
                .header(HttpHeaders.LOCATION, location(command.id).getPath()); 
    }

    @Get(value = "/list") 
    open fun list(@Valid pageable: Pageable ): MutableList<Genre>  { 
        return genreRepository.findAll(pageable).getContent()
    }

    @Post 
    open fun save(@Body("name") @NotBlank name: String, 
        @Body("id") @NotBlank id: Long): HttpResponse<Genre>  
    {
        val genre: Genre = genreRepository.save(id, name)

        return HttpResponse
                .created(genre)
                .headers { it.location(location(genre.id)) }
    }

    @Post("/ex") 
    open fun saveExceptions(@Body @NotBlank name: String): HttpResponse<Genre>  {
        try {
            val genre: Genre = genreRepository.saveWithException(name)
            return HttpResponse
                    .created(genre)
                    .headers { it.location(location(genre.id)) }
        } catch (e: DataAccessException) {
            return HttpResponse.noContent<Genre>();
        }
    }

    @Delete("/{id}") 
    @Status(HttpStatus.NO_CONTENT)
    open fun delete(id: Long) {
        genreRepository.deleteById(id);
    }

    protected fun location(id: Long): URI {
        return URI.create("/genres/" + id)
    }

    protected fun location(genre: Genre ): URI {
        return location(genre.id)
    }

}