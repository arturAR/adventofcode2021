package year2021.days

import java.io.File
import java.util.*

fun getFile(path: String): File = File(Objects.requireNonNull(object{}::class.java.classLoader.getResource(path)).path)