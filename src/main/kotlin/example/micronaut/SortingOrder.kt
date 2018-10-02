package example.micronaut

enum class SortingOrder {
    ASC, DESC;

    companion object {

        val DEFAULT_SORTING_ORDER = SortingOrder.ASC

        fun of(str: String?): SortingOrder {
            if (str != null) {
                if (ASC.toString().equals(str, ignoreCase = true)) {
                    return ASC
                }
                if (DESC.toString().equals(str, ignoreCase = true)) {
                    return DESC
                }
            }
            return DEFAULT_SORTING_ORDER
        }
    }
}