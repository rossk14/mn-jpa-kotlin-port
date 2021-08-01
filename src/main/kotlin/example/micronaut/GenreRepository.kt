package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JdbcRepository(dialect = Dialect.H2) 
@Transactional
interface GenreRepository : PageableRepository<Genre, Long> { 

    fun save(@NonNull @NotBlank id: Long, @NonNull @NotBlank name: String) : Genre

    @Transactional
    fun saveWithException(@NonNull @NotBlank name: String) : Genre {
        save(0, name);
        throw DataAccessException("test exception")
    }

    fun update(@NonNull @NotNull @Id id: Long, @NonNull @NotBlank name: String) : Int

}