package day20

import java.io.File

private const val LIGHT_PIXEL = '#'
private const val DARK_PIXEL = '.'

fun parseInput(lines: List<String>): Pair<String, List<String>> = lines.first() to lines.drop(2)

private fun square3x3(center: Pair<Int, Int>): List<Pair<Int, Int>> =
  listOf(
    // sorted according to the particular order in which the square has to be traversed
    -1 to -1, 0 to -1, 1 to -1,
    -1 to 0, 0 to 0, 1 to 0,
    -1 to 1, 0 to 1, 1 to 1,
  ).map { (x, y) -> center.first + x to center.second + y }

fun enhanceImage(imageEnhancement: String, image: List<String>, iterations: Int): List<String> {
  fun computePixel(position: Pair<Int, Int>, image: List<String>, default: Char): Char {
    val index = square3x3(position)
      .map { (x, y) -> image.getOrNull(y)?.getOrNull(x) ?: default }
      .fold(0) { acc, pixel -> acc.shl(1) + if (pixel == LIGHT_PIXEL) 1 else 0 }
    return imageEnhancement[index]
  }

  fun iterate(image: List<String>, default: Char): List<String> {
    // the canvas extends in all directions indefinitely so the 'default' char in
    // the position 'out of the frame' is also subject to image enhancement effects
    // the default is the one that should be used when checking for an adjacent that's
    // out of the frame. This default will change depending on the algorithm
    val canvasWidth = image.firstOrNull()?.length ?: 0
    val blankLine = listOf(default.toString().repeat(canvasWidth))
    val expandedImage = blankLine + image + blankLine
    val outputImage = mutableListOf<String>()
    for ((y, line) in expandedImage.withIndex()) {
      val builder = StringBuilder()
      for (x in -1..(line.length + 1)) {
        builder.append(computePixel(x to y, expandedImage, default))
      }
      outputImage.add(builder.toString())
    }
    return outputImage
  }

  var default = DARK_PIXEL
  var outputImage = image
  repeat(iterations) {
    outputImage = iterate(outputImage, default)
    default = imageEnhancement[if (default == LIGHT_PIXEL) 511 else 0]
  }
  return outputImage
}

fun countLightPixels(image: List<String>): Int = image.sumOf { it.count { pixel -> pixel == LIGHT_PIXEL } }

fun main() {
  File("./input/day20.txt").useLines { lines ->
    val (enhancement, image) = parseInput(lines.toList())
    val output = enhanceImage(enhancement, image, 50)
    println(countLightPixels(output))
  }
}
