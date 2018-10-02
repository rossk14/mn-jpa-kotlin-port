package example.micronaut.book


import example.micronaut.domain.Book
import example.micronaut.domain.Genre
import java.util.Optional

interface BooksRepository {

    fun findAllBooksByGenre(genreId: Long?): List<Book>

    fun save(isbn: String, name: String, genre: Genre): Book

    fun findById(id: Long?): Optional<Book>

    fun deleteById(id: Long?)

    fun findAll(): List<Book>

    fun update(id: Long?, isbn: String, name: String, genre: Genre): Int

}