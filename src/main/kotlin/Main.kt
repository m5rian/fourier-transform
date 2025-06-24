import extensionUtilities.bufferedImageFromGrayscale2DArray
import extensionUtilities.toGrayscale2DArray
import java.io.File
import javax.imageio.ImageIO
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main() {
    val file = File("/Users/marian/Documents/Code/fourier-transform/img.png")
    require(file.exists()) { "File does not exist!" }

    val image = ImageIO.read(file)
    //val image = generateSinusoidalGratingImage(128, 128, 7.0)
    val spatialDataOriginal = image.toGrayscale2DArray()

    val dft = DFT()
    val frequencyData = dft.toFrequencyDomain(spatialDataOriginal)
        // Use amplitude for visualization
        .map { row -> row.map { it.amplitude }.toDoubleArray() }.toTypedArray()
        .transformLogarithmic()
        .normalizeLinear(UByte.MAX_VALUE.toInt())
    //val spatialData = dft.toSpatialDomain(frequencyData)


    val timestamp = Clock.System.now().epochSeconds
    val originalImage = bufferedImageFromGrayscale2DArray(image.width, image.height, spatialDataOriginal)
    val frequencyImage = bufferedImageFromGrayscale2DArray(image.width, image.height, frequencyData)
    //val spatialImage = bufferedImageFromGrayscale2DArray(image.width, image.height, spatialData)
    ImageIO.write(originalImage, "png", File("./output-${timestamp}_original.png"))
    ImageIO.write(frequencyImage, "png", File("./output-${timestamp}_frequency.png"))
    //ImageIO.write(spatialImage, "png", File("./output-${timestamp}_spatial.png"))
    println("Wrote images to disk with timestamp: $timestamp")
}