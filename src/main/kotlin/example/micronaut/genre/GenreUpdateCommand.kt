package example.micronaut.genre


import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class GenreUpdateCommand (@NotNull var id: Long?, @NotNull @NotBlank var name: String?)