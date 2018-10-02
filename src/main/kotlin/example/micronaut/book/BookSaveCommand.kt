package example.micronaut.book


import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import java.util.Objects

class BookSaveCommand {

    @NotNull
    @NotBlank
    lateinit var name: String

    @NotNull
    @NotBlank
    lateinit var isbn: String

    @NotNull
    var genreId: Long? = null

    constructor() {}

    constructor(isbn: String, name: String, genreId: Long?) {
        this.isbn = isbn
        this.name = name
        this.genreId = genreId
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as BookSaveCommand?
        return name == that!!.name &&
                isbn == that.isbn &&
                genreId == that.genreId
    }

    override fun hashCode(): Int {
        return Objects.hash(name, isbn, genreId)
    }
}
