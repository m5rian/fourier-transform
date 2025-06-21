import kotlin.math.ln1p
import kotlin.math.roundToInt

fun IntArray.normalizeLinear(maxValue: Int, limit: Int) {
    for (i in indices) {
        this[i] = ((this[i].toDouble() / maxValue) * limit).roundToInt()
    }
}

fun IntArray.normalizeLogarithmic(maxValue: Int, limit: Int) {
    for (i in indices) {
        this[i] = ((ln1p(this[i].toDouble()) / ln1p(maxValue.toDouble())) * limit).roundToInt()
    }
}

fun IntArray.transformLogarithmic(maxValue: Int, limit: Int) {
    for (i in indices) {
       this[i] = ln1p(this[i].toDouble()).roundToInt()
    }
}