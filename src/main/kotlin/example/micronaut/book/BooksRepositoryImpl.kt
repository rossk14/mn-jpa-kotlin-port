package example.micronaut.book

import example.micronaut.domain.Book
import example.micronaut.domain.Genre
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional

import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.util.Optional

@Singleton
open class BooksRepositoryImpl(@param:CurrentSession @field:PersistenceContext
                          private val entityManager: EntityManager) : BooksRepository {

    @Transactional
    override fun save(isbn: String, name: String, genre: Genre): Book {
        val book = Book(isbn, name, genre)
        entityManager.persist(book)
        return book
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long?): Optional<Book> {
        return Optional.ofNullable(entityManager.find(Book::class.java, id))
    }

    @Transactional(readOnly = true)
    override fun findAllBooksByGenre(genreId: Long?): List<Book> {
        return entityManager
                .createQuery("SELECT b FROM Book b JOIN FETCH b.genre g WHERE g.id = :genreid", Book::class.java)
                .setParameter("genreid", genreId)
                .resultList
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<Book> {
        return entityManager
                .createQuery("SELECT b FROM Book b", Book::class.java)
                .resultList
    }

    @Transactional
    override fun update(id: Long?, isbn: String, name: String, genre: Genre): Int {
        return entityManager.createQuery("UPDATE Book b SET isbn = :isbn, name = :name, genre = :genre where id = :id")
                .setParameter("name", name)
                .setParameter("isbn", isbn)
                .setParameter("genre", genre)
                .setParameter("id", id)
                .executeUpdate()
    }

    @Transactional
    override fun deleteById(id: Long?) {
        findById(id).ifPresent { book -> entityManager.remove(book) }
    }
}
