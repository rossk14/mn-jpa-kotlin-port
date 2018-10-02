package example.micronaut.book

import example.micronaut.domain.Book
import example.micronaut.domain.Genre
import example.micronaut.genre.GenreRepository
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import java.net.URI
import javax.validation.Valid

@Validated
@Controller("/book")
open class BookController (booksRepository: BooksRepository, genreRepository: GenreRepository) {

    /*
        the following is non-idiomatic but included to resolve AOP error on generated getters/setters
        with advice from here, https://stackoverflow.com/questions/38492103/override-getter-for-kotlin-data-class
     */
    open val booksRepository: BooksRepository = booksRepository
    open val genreRepository: GenreRepository = genreRepository

    @Get("/genres/{id}")
    open fun listByGenre(id: Long?): List<Book> {
        return booksRepository.findAllBooksByGenre(id)
    }

    @Put("/")
    open fun update(@Body @Valid command: BookUpdateCommand): HttpResponse<*> {
        val genreOptional = genreRepository.findById(command.genreId)
        return genreOptional.map({ genre: Genre ->
            booksRepository.update(command.id, command.isbn, command.name, genre)
            HttpResponse.noContent<Any>().header(HttpHeaders.LOCATION, location(command.id).path)
        }).orElse(HttpResponse.badRequest<Any>())
    }

    @Get("/")
    open fun list(): List<Book> {
        return booksRepository.findAll()
    }

    @Get("/{id}")
    internal open fun show(id: Long?): Book? {
        return booksRepository
                .findById(id)
                .orElse(null)
    }

    @Delete("/{id}")
    internal open fun delete(id: Long?): HttpResponse<*> {
        booksRepository.deleteById(id)
        return HttpResponse.noContent<Any>()
    }

    @Post("/")
    internal open fun save(@Body @Valid cmd: BookSaveCommand): HttpResponse<Book> {
        val genreOptional = genreRepository.findById(cmd.genreId)
        return genreOptional.map({ genre: Genre ->
            val book = booksRepository.save(cmd.isbn, cmd.name, genre)
            HttpResponse
                    .created(book)
                    .headers { headers -> headers.location(location(book)) }
        }).orElse(HttpResponse.badRequest<Book>())
    }

    protected open fun location(book: Book): URI {
        return location(book.id)
    }

    protected open fun location(id: Long?): URI {
        return URI.create("/books/" + id!!)
    }

}