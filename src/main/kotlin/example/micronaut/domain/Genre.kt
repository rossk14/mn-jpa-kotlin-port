package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull
import java.util.HashSet

@Entity
@Table(name = "genre")
class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    var name: String? = null

    @JsonIgnore
    @OneToMany(mappedBy = "genre")
    var books: Set<Book> = HashSet()

    constructor() {}

    constructor(@NotNull name: String) {
        this.name = name
    }

    override fun toString(): String {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                '}'.toString()
    }
}
