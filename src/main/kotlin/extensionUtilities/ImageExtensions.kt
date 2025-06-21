package extensionUtilities

import java.awt.image.BufferedImage

fun BufferedImage.toGrayscale2DArray(): Array<IntArray> {
    val width = this.width
    val height = this.height
    val grayPixels = Array(height) { IntArray(width) }

    for (y in 0 until height) {
        for (x in 0 until width) {
            val rgb = this.getRGB(x, y)
            val r = (rgb shr 16) and 0xFF
            val g = (rgb shr 8) and 0xFF
            val b = rgb and 0xFF
            val gray = (r + g + b) / 3
            grayPixels[y][x] = (gray shl 16) or (gray shl 8) or gray // RGB format
        }
    }

    return grayPixels
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

fun BufferedImage.mapPixels(transform: (Int, Int) -> Int): BufferedImage {
    val width = this.width
    val height = this.height
    val newImage = BufferedImage(width, height, this.type)

    for (y in 0 until height) {
        for (x in 0 until width) {
            newImage.setRGB(x, y, transform(x, y))
        }
    }

    return newImage
}

fun BufferedImage.getGrey(x: Int, y: Int): Int {
    val rgb = this.getRGB(x, y)
    val r = (rgb shr 16) and 0xFF
    val g = (rgb shr 8) and 0xFF
    val b = rgb and 0xFF
    return (r + g + b) / 3 // Return the average as the gray value
}

fun BufferedImagefromArrayGray(width: Int, height: Int, pixels: IntArray): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)

    val raster = image.raster
    for (y in 0 until height) {
        for (x in 0 until width) {
            val gray = pixels[y * width + x].coerceIn(0, 255)
            raster.setSample(x, y, 0, gray) // Channel 0 for grayscale
        }
    }
    return image
}

fun Int.toGrayscale(): Int {
    val gray = this and 0xFF
    return (gray shl 16) or (gray shl 8) or gray // Convert gray to RGB format
}