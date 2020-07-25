package ru.skillbranch.kotlinexample.extensions

inline fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    var bool = false
    val iterator = this.listIterator(size)
    while (iterator.hasPrevious()) {
        val element = iterator.previous()
        if (predicate(element)) bool = true
        if (bool) list.add(element)
    }
    list.reverse()
    return list.dropLast(1)
}