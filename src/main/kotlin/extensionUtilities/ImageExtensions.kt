package extensionUtilities

import java.awt.image.BufferedImage
import kotlin.math.PI
import kotlin.math.sin

fun BufferedImage.toGrayscale2DArray(): Array<IntArray> {
    val width = this.width
    val height = this.height
    val grayPixels = Array(height) { IntArray(width) }

    for (y in 0 until height) {
        for (x in 0 until width) {
            val rgb = this.getRGB(x, y)
            val grayscale = rgbToGrayscale(rgb)
            grayPixels[y][x] = grayscale
        }
    }

    return grayPixels
}

fun rgbToGrayscale(rgb: Int): Int {
    val r = (rgb shr 16) and 0xFF
    val g = (rgb shr 8) and 0xFF
    val b = rgb and 0xFF

    // Calculate the luminance (grayscale) using standard NTSC conversion formula
    val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
    return gray
}

fun Array<IntArray>.forEachPixel(transform: (x: Int, y: Int, rgb: Int) -> Unit) {
    for (y in indices) {
        for (x in this[y].indices) {
            transform(x, y, this[y][x])
        }
    }
}

val Array<IntArray>.width: Int
    get() = this.first().size

val Array<IntArray>.height: Int
    get() = this.size

val Array<DoubleArray>.width: Int
    get() = this.first().size

val Array<DoubleArray>.height: Int
    get() = this.size


fun bufferedImageFromGrayscale2DArray(width: Int, height: Int, pixels: Array<IntArray>): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
    val raster = image.raster

    for (y in 0 until height) {
        for (x in 0 until width) {
            val gray = pixels[y][x].coerceIn(0, 255)
            raster.setSample(x, y, 0, gray) // Channel 0 for grayscale
        }
    }
    return image
}

fun generateSinusoidalGratingImage(
    width: Int,
    height: Int,
    frequency: Double, // number of cycles across the image width
    phase: Double = 0.0 // in radians
): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)

    for (y in 0 until height) {
        for (x in 0 until width) {
            // Compute normalized value of sine wave: from 0 to 255
            val radians = 2 * PI * frequency * x / width + phase
            val intensity = ((sin(radians) + 1) * 127.5).toInt().coerceIn(0, 255)
            val rgb = (intensity shl 16) or (intensity shl 8) or intensity
            image.setRGB(x, y, rgb)
        }
    }

    return image
}
