package example.micronaut

class PaginationArguments(offset: Int?, max: Int?) {
    var offset: Int? = null
        internal set
    var max: Int? = null
        internal set

    init {
        this.offset = if (offset == null || offset < 0) 0 else offset
        this.max = max
    }

}