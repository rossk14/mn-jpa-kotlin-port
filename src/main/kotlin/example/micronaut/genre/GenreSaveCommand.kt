package example.micronaut.genre

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class GenreSaveCommand (@NotNull @NotBlank var name: String)