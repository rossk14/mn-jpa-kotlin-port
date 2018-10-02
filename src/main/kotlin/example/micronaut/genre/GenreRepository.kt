package example.micronaut.genre

import example.micronaut.PaginationArguments
import example.micronaut.SortingArguments
import example.micronaut.domain.Genre
import java.util.Optional

interface GenreRepository {

    fun findById(id: Long?): Optional<Genre>

    fun save(name: String): Genre

    fun deleteById(id: Long?)

    fun findAll(paginationArgs: PaginationArguments?, sortingArgs: SortingArguments?): List<Genre>

    fun update(id: Long?, name: String): Int
}