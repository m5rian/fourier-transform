import extensionUtilities.columns
import extensionUtilities.rows
import org.kotlinmath.Complex
import org.kotlinmath.I
import org.kotlinmath.exp
import org.kotlinmath.times
import kotlin.math.PI
import kotlin.math.roundToInt

class DFT : FourierTransform() {

    override fun toFrequencyDomain(spatialData: Array<IntArray>): Array<Array<FrequencyDomain>> {
        val frequencyData = Array(spatialData.rows) {
            Array(spatialData.columns) {
                FrequencyDomain(0.I)
            }
        }

        for (freqX in 0 until spatialData.columns) {
            for (freqY in 0 until spatialData.rows) {
                val complex = f(freqX, freqY, spatialData) // Complex number result
                // Shift the frequency to the center of the image (better for visualization)
                val shiftedX = (freqX + spatialData.columns / 2) % spatialData.columns
                val shiftedY = (freqY + spatialData.rows / 2) % spatialData.rows

                frequencyData[shiftedY][shiftedX] = FrequencyDomain(complex)
            }
        }

        return frequencyData
    }

    private fun f(freqX: Int, freqY: Int, spatialData: Array<IntArray>): Complex {
        var sum = 0.I

        for (spaceY in 0 until spatialData.rows) {
            for (spaceX in 0 until spatialData.columns) {
                val pixelValue = spatialData[spaceY][spaceX]

                val normalizedX = freqX * spaceX / spatialData.columns.toDouble()
                val normalizedY = freqY * spaceY / spatialData.rows.toDouble()

                val angle = 2.0 * PI * (normalizedX + normalizedY)
                val exponential = exp(I * -angle)

                sum += pixelValue * exponential
            }
        }

        return sum
    }

    override fun toSpatialDomain(frequencyData: Array<Array<FrequencyDomain>>): Array<IntArray> {
        val spatialData = Array(frequencyData.rows) { IntArray(frequencyData.columns) }

        for (spaceY in 0 until frequencyData.rows) {
            for (spaceX in 0 until frequencyData.columns) {
                val complex = fInverse(spaceY, spaceX, frequencyData)

                // Normalize by total number of samples (width * height)
                val value = (complex.re / (frequencyData.rows * frequencyData.columns)).roundToInt()

                // Clamp value if needed (depending on image data range, e.g., 0..255)
                spatialData[spaceY][spaceX] = value.coerceIn(0, 255)
            }
        }

        return spatialData
    }

    private fun fInverse(spaceY: Int, spaceX: Int, frequencyData: Array<Array<FrequencyDomain>>): Complex {
        var sum = 0.I

        for (freqY in 0 until frequencyData.rows) {
            for (freqX in 0 until frequencyData.columns) {
                val shiftedX = (freqX + frequencyData.columns / 2) % frequencyData.columns
                val shiftedY = (freqY + frequencyData.rows / 2) % frequencyData.rows

                val freqValue = frequencyData[shiftedY][shiftedX].complex

                val normalizedX = freqX * spaceX / frequencyData.columns.toDouble()
                val normalizedY = freqY * spaceY / frequencyData.rows.toDouble()

                val angle = 2.0 * PI * (normalizedX + normalizedY)
                val exponential = exp(I * angle) // positive sign here for inverse

                sum += freqValue * exponential
            }
        }

        return sum
    }

}