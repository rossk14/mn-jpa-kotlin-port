package example.micronaut.book


import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import java.util.Objects

class BookUpdateCommand {

    @NotNull
    var id: Long? = null

    @NotNull
    @NotBlank
    lateinit var name: String

    @NotNull
    @NotBlank
    lateinit var isbn: String

    @NotNull
    var genreId: Long? = null

    constructor() {}

    constructor(id: Long?, isbn: String, name: String, genreId: Long?) {
        this.id = id
        this.isbn = isbn
        this.name = name
        this.genreId = genreId
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as BookUpdateCommand?
        return id == that!!.id &&
                name == that.name &&
                isbn == that.isbn &&
                genreId == that.genreId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, isbn, genreId)
    }
}
