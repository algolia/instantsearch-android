package helper

typealias Hierarchy<T> = Map<String, List<T>> //TODO: Use client's typealias
typealias RefinementListListener<T> = (List<T>) -> Unit
typealias RefinementListener<T> = (T?) -> Unit
typealias HierarchyRefinementListener<T> = (Hierarchy<T>) -> Unit
