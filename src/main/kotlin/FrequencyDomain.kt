import extensionUtilities.abs
import org.kotlinmath.Complex
import kotlin.math.atan2

data class FrequencyDomain(
    val complex: Complex
) {
    val amplitude = complex.abs()
    val phase = atan2(complex.im, complex.re)
}
