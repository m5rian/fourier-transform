import extensionUtilities.generateSinusoidalGratingImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val image = generateSinusoidalGratingImage(512, 512, 7.0)
    val timestamp = System.currentTimeMillis()
    ImageIO.write(image, "png", File("./images/sin-${timestamp}_original.png"))
    println("Wrote images to disk with timestamp: $timestamp")
}