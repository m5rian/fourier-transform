import extensionUtilities.height
import extensionUtilities.width
import kotlin.math.log10
import kotlin.math.roundToInt

fun Array<DoubleArray>.normalizeLinear(limit: Int): Array<IntArray> {
    val maxValue = this.maxOf { row -> row.max() }

    val scale = if (maxValue > 0) limit.toDouble() / maxValue else 1.0

    val normalized = Array(height) { IntArray(width) { 0 } }
    for (y in indices) {
        for (x in this[y].indices) {
            val value = this[y][x]
            normalized[y][x] = (value * scale).roundToInt().coerceIn(0, limit)
        }
    }

    return normalized
}

fun Array<DoubleArray>.transformLogarithmic(): Array<DoubleArray> {
    val logarithmic = Array(height) { DoubleArray(width) { 0.0 } }

    for (y in indices) {
        for (x in this[y].indices) {
            logarithmic[y][x] = log10(1 + this[y][x])
        }
    }

    return logarithmic
}

fun Array<IntArray>.toDisplayImage(): Array<IntArray> {
    val h = size
    val w = this[0].size
    // first, build log‐amplitude
    val logAmp = Array(h) { IntArray(w) }
    var maxLog = 0.0
    for (y in 0 until h) {
        for (x in 0 until w) {
            val v = log10(1.0 + this[y][x].toDouble())
            logAmp[y][x] = v.roundToInt()
            if (v > maxLog) maxLog = v
        }
    }
    // then scale to 0…255
    val out = Array(h) { IntArray(w) }
    for (y in 0 until h) {
        for (x in 0 until w) {
            out[y][x] = ((logAmp[y][x] / maxLog) * 255.0).roundToInt()
        }
    }
    return out
}
