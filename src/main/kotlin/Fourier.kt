import extensionUtilities.*
import org.kotlinmath.I
import org.kotlinmath.exp
import org.kotlinmath.times
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main() {
    val file = File("/Users/marian/Documents/Code/fourier-transform/input_125.jpg")
    require(file.exists()) { "File does not exist!" }

    val image = ImageIO.read(file)
    // Array of rows of pixels in grayscale format
    val pixels = image.toGrayscale2DArray()

    val frequencyData = IntArray(pixels.size * pixels[0].size) { 0 }
    frequencyData.apply {
        var maxAmplitude = 0
        pixels.forEachPixel { freqX, freqY, rgb ->
            val amplitude = calculateFrequency(freqX, freqY, pixels).roundToInt()
            if (amplitude > maxAmplitude) maxAmplitude = amplitude

            // Shift the frequency to the center of the image (better for visualization)
            val shiftedX = (freqX + image.width / 2) % image.width
            val shiftedY = (freqY + image.height / 2) % image.height
            this[shiftedY * image.width + shiftedX] = amplitude
        }
        normalizeLogarithmic(maxAmplitude, 255)
        /*this.forEachIndexed { i, v ->
            val x = i % image.width
            val y = i / image.width
            println("($x,$y) -> $v")
        }*/
    }
    val frequencyImage = BufferedImagefromArrayGray(image.width, image.height, frequencyData)

    val outputFile = File("./output-${Clock.System.now().epochSeconds}.png")
    ImageIO.write(frequencyImage, "png", outputFile)
    println("Fourier transform completed and saved to ${outputFile.name}")
}

fun calculateFrequency(freqX: Int, freqY: Int, pixels: Array<IntArray>): Double {
    var sum = 0.I
    for (spaceX in 0 until pixels.width) {
        for (spaceY in 0 until pixels.height) {
            val pixelValue = pixels[spaceY][spaceX].toDouble()

            val normalizedX = freqX * spaceX / pixels.width.toDouble()
            val normalizedY = freqY * spaceY / pixels.height.toDouble()

            val angle = 2 * PI * (normalizedX + normalizedY)
            val exponential = exp(I * -angle)

            sum += pixelValue * exponential
        }
    }

    val magnitude = sum.abs()
    //println("($freqX,$freqY) -> $magnitude")
    return magnitude
}