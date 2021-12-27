package year2021.days.day12

import year2021.days.getFile

private const val FILENAME = "day12/input.txt"
private const val TEST_FILENAME = "day12/test_input.txt"
private const val TEST_FILENAME_2 = "day12/test_input2.txt"
private const val TEST_FILENAME_3 = "day12/test_input3.txt"

fun main() {
    val lines = getFile(FILENAME).readLines()

    println(solve1Task(lines))
    println(solve2Task(lines))
}

fun solve1Task(lines: List<String>): Int {
    val filter = { node: Graph.Node<Cave>, visited: Map<Cave, Int> ->
        visited.getOrDefault(node.key, 0) < 1
    }

    return solveTask(lines, filter)
}

fun solve2Task(lines: List<String>): Int {
    val filter = { node: Graph.Node<Cave>, visited: Map<Cave, Int> ->
        visited.getOrDefault(node.key, 0) < if (visited.any { it.value >= 2 }) 1 else 2
    }

    return solveTask(lines, filter)
}

fun solveTask(lines: List<String>, filter: (Graph.Node<Cave>, Map<Cave, Int>) -> Boolean): Int {
    val graph = lines.map { it.split("-").let { (a, b) -> Cave(a) to Cave(b) } }
        .fold(Graph<Cave, Int>()) { graph, (from, to) ->
            graph.add(from, to, 0)
            graph
        }

    return mutableListOf<List<Cave>>()
        .also { recurse(graph, Cave("start"), Cave("end"), emptyMap(), emptyList(), it, filter) }
        .size
}

private fun recurse(
    graph: Graph<Cave, Int>,
    from: Cave,
    to: Cave,
    visited: Map<Cave, Int>,
    path: List<Cave>,
    paths: MutableList<List<Cave>>,
    nodeFilter: (Graph.Node<Cave>, Map<Cave, Int>) -> Boolean
) {
    if (from == to) {
        paths += path
        return
    }

    val newVisited = if (!from.big) {
        visited + (from to visited.getOrDefault(from, 0) + 1)
    } else visited

    graph.nodes(from)
        .filterNot { it.key.id == "start" }
        .filter { nodeFilter(it, newVisited) }
        .forEach { recurse(graph, it.key, to, newVisited, path + it.key, paths, nodeFilter) }
}

data class Cave(val id: String, val big: Boolean = id.uppercase() == id)

class Graph<N, E> {
    data class Node<N>(val key: N)
    data class Edge<N, E>(val value: E, val to: Node<N>)

    private val nodes: MutableMap<N, Node<N>> = mutableMapOf()
    private val nodeToEdge: MutableMap<N, MutableSet<Edge<N, E>>> = mutableMapOf()

    fun add(from: N, to: N, edge: E) {
        nodes.computeIfAbsent(from) { node(it) }
        nodes.computeIfAbsent(to) { node(it) }

        setEdge(from, to, edge)
        setEdge(to, from, edge)
    }

    fun nodes(key: N) = edges(key).map { it.to }

    private fun setEdge(from: N, to: N, e: E) {
        nodeToEdge.computeIfAbsent(from) { mutableSetOf() } += edge(e, get(to))
    }

    private fun <T> node(value: T) = Node(value)
    private fun <E, N> edge(value: E, node: Node<N>) = Edge(value, node)
    private fun get(key: N) = nodes[key] ?: throw NoSuchElementException("No node with key '$key'")
    private fun edges(key: N): Set<Edge<N, E>> = nodeToEdge[key] ?: emptySet()
}