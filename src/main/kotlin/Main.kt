import extensionUtilities.bufferedImageFromGrayscale2DArray
import extensionUtilities.generateSinusoidalGratingImage
import extensionUtilities.toGrayscale2DArray
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.PI
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main() {
    val file = File("./images/dom.png")
    //require(file.exists()) { "File does not exist!" }

    //val image = ImageIO.read(file)
    val image = generateSinusoidalGratingImage(1024, 1024, 14.0, PI/2)
    val spatialDataOriginal = image.toGrayscale2DArray()

    val dft = FFTCooleyTukey()
    val frequencyData = dft.toFrequencyDomain(spatialDataOriginal)
    val amplitudeData = frequencyData
        // Use amplitude for visualization
        .map { row -> row.map { it.amplitude }.toDoubleArray() }.toTypedArray()
        .transformSquareRoot()
        .normalizeLinear(UByte.MAX_VALUE.toInt())
    //val spatialData = dft.toSpatialDomain(frequencyData)


    val timestamp = Clock.System.now().epochSeconds
    val originalImage = bufferedImageFromGrayscale2DArray(image.width, image.height, spatialDataOriginal)
    val frequencyImage = bufferedImageFromGrayscale2DArray(image.width, image.height, amplitudeData)
    //val spatialImage = bufferedImageFromGrayscale2DArray(image.width, image.height, spatialData)
    ImageIO.write(originalImage, "png", File("./images/output-${timestamp}_original.png"))
    ImageIO.write(frequencyImage, "png", File("./images/output-${timestamp}_frequency.png"))
    //ImageIO.write(spatialImage, "png", File("./images/output-${timestamp}_spatial.png"))
    println("Wrote images to disk with timestamp: $timestamp")
}