import extensionUtilities.columns
import extensionUtilities.rows
import extensionUtilities.transposed
import org.kotlinmath.*
import kotlin.math.PI
import kotlin.math.roundToInt

class FFTCooleyTukey : FourierTransform() {

    override fun toFrequencyDomain(spatialData: Array<IntArray>): Array<Array<FrequencyDomain>> {
        val rows = spatialData.rows
        val columns = spatialData.columns

        var input = Array(spatialData.rows) { row ->
            Array(spatialData.columns) { col ->
                complex(spatialData[row][col], 0.0)
            }
        }

        // Apply FFT on each row
        for (row in 0 until rows) {
            input[row] = f(input[row])
        }

        input = input.transposed() // Convert to column orientation for FFT on columns

        for (col in 0 until columns) {
            input[col] = f(input[col])
        }

        val result = input.transposed() // Convert back to original orientation

        val shifted = Array(rows) { Array(columns) { FrequencyDomain(0.I) } }
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                // Shift the frequency to the center of the image (better for visualization)
                val shiftedX = (row + rows / 2) % rows
                val shiftedY = (col + columns / 2) % columns
                shifted[shiftedX][shiftedY] = FrequencyDomain(result[row][col])
            }
        }

        return shifted
    }

    private fun f(a: Array<Complex>): Array<Complex> {
        val n = a.size
        require(n and (n - 1) == 0) { "FFT input size must be a power of two" }

        if (n <= 1) return a

        val even = f(Array(n / 2) { a[2 * it] })
        val odd = f(Array(n / 2) { a[2 * it + 1] })

        val combined = Array(n) { 0.I }
        for (i in 0 until n / 2) {
            val omega = omega(i, n) * odd[i]
            combined[i] = even[i] + omega
            combined[i + n / 2] = even[i] - omega
        }

        return combined
    }

    private fun omega(i: Int, n: Int): Complex {
        return exp(-2.0 * PI * I * i / n)
    }

    override fun toSpatialDomain(frequencyData: Array<Array<FrequencyDomain>>): Array<IntArray> {
        val rows = frequencyData.rows
        val columns = frequencyData.columns

        // Undo frequency shift
        val unshifted = Array(rows) { row ->
            Array(columns) { col ->
                val shiftedX = (row + rows / 2) % rows
                val shiftedY = (col + columns / 2) % columns
                frequencyData[shiftedX][shiftedY].complex
            }
        }

        // Apply inverse FFT on each column
        var transformed = unshifted.transposed()

        for (col in 0 until columns) {
            transformed[col] = fInverse(transformed[col])
        }

        // Apply inverse FFT on each row
        transformed = transformed.transposed()

        for (row in 0 until rows) {
            transformed[row] = fInverse(transformed[row])
        }

        // Normalize and convert to Int
        val scale = rows * columns
        return Array(rows) { row ->
            IntArray(columns) { col ->
                transformed[row][col].re.roundToInt() / scale
            }
        }
    }

    private fun fInverse(a: Array<Complex>): Array<Complex> {
        val n = a.size
        require(n and (n - 1) == 0) { "FFT input size must be a power of two" }

        if (n <= 1) return a

        val even = fInverse(Array(n / 2) { a[2 * it] })
        val odd = fInverse(Array(n / 2) { a[2 * it + 1] })

        val combined = Array(n) { 0.I }
        for (i in 0 until n / 2) {
            val omega = omegaInverse(i, n) * odd[i]
            combined[i] = even[i] + omega
            combined[i + n / 2] = even[i] - omega
        }

        return combined
    }

    private fun omegaInverse(i: Int, n: Int): Complex {
        return exp(2.0 * PI * I * i / n)
    }

}