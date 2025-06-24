import extensionUtilities.abs
import extensionUtilities.height
import extensionUtilities.width
import org.kotlinmath.Complex
import org.kotlinmath.I
import org.kotlinmath.exp
import org.kotlinmath.times
import kotlin.math.PI
import kotlin.math.roundToInt

class DFT : FourierTransform() {

    override fun toFrequencyDomain(spatialData: Array<IntArray>): Array<IntArray> {
        val frequencyData = Array(spatialData.height) { IntArray(spatialData.width) { 0 } }

        for (freqX in 0 until spatialData.width) {
            for (freqY in 0 until spatialData.height) {
                //println("f($freqX, $freqY)")
                val frequency = f(freqX, freqY, spatialData)
                val amplitude = frequency.abs().toInt()

                // Shift the frequency to the center of the image (better for visualization)
                val shiftedX = (freqX + spatialData.width / 2) % spatialData.width
                val shiftedY = (freqY + spatialData.height / 2) % spatialData.height

                frequencyData[shiftedY][shiftedX] = amplitude
            }
        }

        return frequencyData
    }

    override fun f(freqX: Int, freqY: Int, spatialData: Array<IntArray>): Complex {
        var sum = 0.I

        for (spaceX in 0 until spatialData.width) {
            for (spaceY in 0 until spatialData.height) {
                val pixelValue = spatialData[spaceY][spaceX].toDouble()

                val normalizedX = freqX * spaceX / spatialData.width.toDouble()
                val normalizedY = freqY * spaceY / spatialData.height.toDouble()

                val angle = 2 * PI * (normalizedX + normalizedY)
                val exponential = exp(I * -angle)

                sum += pixelValue * exponential
            }
        }

        return sum
    }

    override fun toSpatialDomain(frequencyData: Array<IntArray>): Array<IntArray> {
        val spatialData = Array(frequencyData.height) { IntArray(frequencyData.width) { 0 } }

        for (spaceX in 0 until frequencyData.width) {
            for (spaceY in 0 until frequencyData.height) {
                val value = fInverse(spaceX, spaceY, frequencyData)
                println("fInverse($spaceX, $spaceY) = re:${value.re}, im:${value.im}")
                spatialData[spaceY][spaceX] = value.re.roundToInt()
            }
        }

        return spatialData
    }

    override fun fInverse(spaceX: Int, spaceY: Int, frequencyData: Array<IntArray>): Complex {
        var sum = 0.I

        for (freqX in 0 until frequencyData.width) {
            for (freqY in 0 until frequencyData.height) {
                val pixelValue = frequencyData[freqY][freqX].toDouble()

                val normalizedX = spaceX * freqX / frequencyData.width.toDouble()
                val normalizedY = spaceY * freqY / frequencyData.height.toDouble()

                val angle = 2 * PI * (normalizedX + normalizedY)
                val exponential = exp(I * angle)

                sum += pixelValue * exponential
            }
        }

        return sum * (1.0 / (frequencyData.width * frequencyData.height))
    }

}