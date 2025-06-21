package extensionUtilities

import org.kotlinmath.Complex
import kotlin.math.sqrt

fun Complex.abs(): Double {
    return sqrt(re * re + im * im)
}