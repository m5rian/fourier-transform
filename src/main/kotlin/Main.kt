import extensionUtilities.bufferedImageFromGrayscale2DArray
import extensionUtilities.columns
import extensionUtilities.rows
import extensionUtilities.toGrayscale2DArray
import java.io.File
import javax.imageio.ImageIO
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main() {
    val imageFile = File("./images/square.png")
    require(imageFile.exists()) { "File does not exist!" }
    val image = ImageIO.read(imageFile)

    val filterFile = File("./images/blur_mask.png")
    val filter = ImageIO.read(filterFile).toGrayscale2DArray()

    //val image = generateCombinedSinusoidalGratingImage(1024, 1024, 10.0, 10.0)
    val spatialDataOriginal = image.toGrayscale2DArray()
    val ft = FFTCooleyTukey()
    val frequencyData = ft.toFrequencyDomain(spatialDataOriginal)

    val filteredFrequencyData = Array(frequencyData.rows) { row ->
        Array(frequencyData.columns) { col ->
            val filterPixel = filter[row][col] / 255.0
            val frequencyPixel = frequencyData[row][col]

            FrequencyDomain(  frequencyPixel.complex * filterPixel)
        }
    }

    val amplitudeData = filteredFrequencyData
        // Use amplitude for visualization
        .map { row -> row.map { it.amplitude }.toDoubleArray() }.toTypedArray()
        .transformSquareRoot()
        .normalizeLinear(UByte.MAX_VALUE.toInt())
    val spatialData = ft.toSpatialDomain(filteredFrequencyData)

    val timestamp = Clock.System.now().epochSeconds
    val originalImage = bufferedImageFromGrayscale2DArray(image.width, image.height, spatialDataOriginal)
    val frequencyImage = bufferedImageFromGrayscale2DArray(image.width, image.height, amplitudeData)
    val spatialImage = bufferedImageFromGrayscale2DArray(image.width, image.height, spatialData)
    ImageIO.write(originalImage, "png", File("./images/output-${timestamp}_original.png"))
    ImageIO.write(frequencyImage, "png", File("./images/output-${timestamp}_frequency.png"))
    ImageIO.write(spatialImage, "png", File("./images/output-${timestamp}_spatial.png"))
    println("Wrote images to disk with timestamp: $timestamp")
}