import extensionUtilities.bufferedImageFromGrayscale2DArray
import extensionUtilities.toGrayscale2DArray
import java.io.File
import javax.imageio.ImageIO
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main() {
    val file = File("./images/dom.png")
    //require(file.exists()) { "File does not exist!" }

    val image = ImageIO.read(file)
    val spatialDataOriginal = image.toGrayscale2DArray()
    val timestamp = Clock.System.now().epochSeconds
    val originalImage = bufferedImageFromGrayscale2DArray(image.width, image.height, spatialDataOriginal)
    ImageIO.write(originalImage, "png", File("./images/output-${timestamp}_original.png"))
    println("Wrote images to disk with timestamp: $timestamp")
}