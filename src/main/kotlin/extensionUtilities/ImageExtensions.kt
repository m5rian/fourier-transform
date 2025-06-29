package extensionUtilities

import java.awt.image.BufferedImage
import kotlin.math.PI
import kotlin.math.cos
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
    frequency: Double,   // number of cycles across the image width
    angle: Double,       // angle of the sinusoid in radians, 0 = horizontal
    phase: Double = 0.0  // phase offset in radians
): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)

    val cosA = cos(angle)
    val sinA = sin(angle)

    for (y in 0 until height) {
        for (x in 0 until width) {
            // Project (x,y) onto direction of the wave
            val proj = (x * cosA + y * sinA) / width  // normalized position along wave direction
            val radians = 2 * PI * frequency * proj + phase
            val intensity = ((sin(radians) + 1) * 127.5).toInt().coerceIn(0, 255)
            val rgb = (intensity shl 16) or (intensity shl 8) or intensity
            image.setRGB(x, y, rgb)
        }
    }

    return image
}

fun generateCombinedSinusoidalGratingImage(
    width: Int,
    height: Int,
    frequencyX: Double,   // frequency of horizontal waves (cycles across width)
    frequencyY: Double,   // frequency of vertical waves (cycles across height)
    phaseX: Double = 0.0,
    phaseY: Double = 0.0
): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)

    for (y in 0 until height) {
        for (x in 0 until width) {
            // Horizontal sine wave (along x-axis)
            val radiansX = 2 * Math.PI * frequencyX * (x.toDouble() / width) + phaseX
            val intensityX = Math.sin(radiansX)

            // Vertical sine wave (along y-axis)
            val radiansY = 2 * Math.PI * frequencyY * (y.toDouble() / height) + phaseY
            val intensityY = Math.sin(radiansY)

            // Combine waves by adding (values between -1 and 1)
            val combined = intensityX + intensityY

            // Normalize combined value to 0-255
            // The sum ranges from -2 to 2, so map [-2,2] -> [0,255]
            val intensity = ((combined + 2) * (255.0 / 4.0)).toInt().coerceIn(0, 255)

            val rgb = (intensity shl 16) or (intensity shl 8) or intensity
            image.setRGB(x, y, rgb)
        }
    }

    return image
}
