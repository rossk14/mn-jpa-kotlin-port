package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.micronaut.data.annotation.GeneratedValue
// import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.AutoPopulated
import javax.validation.constraints.NotNull
import javax.persistence.Entity
import javax.persistence.Id
import javax.transaction.Transactional

@Entity
@Transactional
// @EnableTransactionManagement 
open class Genre(@Id @AutoPopulated var id: Long, var name: String) {
    constructor(): this(
        id = 0,
        name = ""
    )
}
