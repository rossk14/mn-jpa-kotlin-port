package example.micronaut.genre

import example.micronaut.PaginationArguments
import example.micronaut.SortingArguments
import example.micronaut.domain.Genre
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional

import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery
import java.util.Arrays
import java.util.Optional

@Singleton // <1>
open class GenreRepositoryImpl(@param:CurrentSession @field:PersistenceContext
                          private val entityManager: EntityManager  // <2>
)// <2>
    : GenreRepository {



    @Transactional(readOnly = true) // <3>
    override fun findById(id: Long?): Optional<Genre> {
        return Optional.ofNullable(entityManager.find(Genre::class.java, id))
    }

    @Transactional // <4>
    override fun save(name: String): Genre {
        val genre = Genre(name)
        entityManager.persist(genre)
        return genre
    }

    @Transactional
    override fun deleteById(id: Long?) {
        findById(id).ifPresent { genre -> entityManager.remove(genre) }
    }

    @Transactional(readOnly = true)
    override fun findAll(paginationArgs: PaginationArguments?, sortingArgs: SortingArguments?): List<Genre> {
        var qlString = "SELECT g FROM Genre as g"
        if (sortingArgs != null) {
            if (sortingArgs.order != null && VALID_PROPERTY_NAMES.contains(sortingArgs.sort)) {
                qlString += " ORDER BY g." + sortingArgs.sort + " " + sortingArgs.order.toString().toLowerCase()
            }
        }
        val query = entityManager.createQuery(qlString, Genre::class.java)
        if (paginationArgs != null) {
            if (paginationArgs.max != null) {
                query.maxResults = paginationArgs.max!!
            }
            if (paginationArgs.offset != null) {
                query.firstResult = paginationArgs.offset!!
            }
        }
        return query.resultList
    }

    @Transactional
    override fun update(id: Long?, name: String): Int {
        return entityManager.createQuery("UPDATE Genre g SET name = :name where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .executeUpdate()
    }

    companion object {

        private val VALID_PROPERTY_NAMES = Arrays.asList("id", "name")
    }
}
