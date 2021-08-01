package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected 
data class GenreUpdateCommand (@NotNull val id: Long, @NotBlank val name: String)
