package example.micronaut.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "book")
class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null

    @NotNull
    @Column(name = "isbn", nullable = false)
    var isbn: String? = null

    @ManyToOne
    var genre: Genre? = null

    constructor() {}

    constructor(@NotNull isbn: String, @NotNull name: String, genre: Genre) {
        this.isbn = isbn
        this.name = name
        this.genre = genre
    }

    override fun toString(): String {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                ", isbn='" + isbn + '\''.toString() +
                ", genre=" + genre +
                '}'.toString()
    }
}