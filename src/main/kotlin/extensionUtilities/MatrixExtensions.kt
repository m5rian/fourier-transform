package extensionUtilities

import FrequencyDomain
import org.kotlinmath.Complex
import org.kotlinmath.I

val Array<IntArray>.rows: Int
    get() = this.size

val Array<IntArray>.columns: Int
    get() = this.first().size


val Array<DoubleArray>.width: Int
    get() = this.first().size

val Array<DoubleArray>.height: Int
    get() = this.size


val Array<Array<FrequencyDomain>>.rows: Int
    get() = this.size

val Array<Array<FrequencyDomain>>.columns: Int
    get() = this.first().size


fun Array<Array<Complex>>.transposed(): Array<Array<Complex>> {
    val rows = this.size
    val columns = this[0].size
    return Array(columns) { x ->
        Array(rows) { y ->
            this[y][x]
        }
    }
}
