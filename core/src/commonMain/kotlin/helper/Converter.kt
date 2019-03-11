package helper


interface Converter<I, O> {

    fun convertIn(input: I): O
    fun convertIn(input: List<I>): List<O> = input.map(::convertIn)
    fun convertOut(input: O): I
    fun convertOut(input: List<O>): List<I> = input.map(::convertOut)
}