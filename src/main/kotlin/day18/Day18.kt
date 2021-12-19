package day18

import java.io.File


sealed interface SnailFishNumber {
  val magnitude: Int
}

class SnailNumber(override var magnitude: Int) : SnailFishNumber {
  val isSplittable: Boolean
    get() = magnitude > 9

  override fun toString(): String = magnitude.toString()

  fun split(parent: SnailPair): SnailPair =
    SnailPair(parent, SnailNumber(magnitude / 2), SnailNumber(magnitude - (magnitude / 2)))
}

class SnailPair(var parent: SnailPair?) : SnailFishNumber {
  constructor(parent: SnailPair?, left: SnailPair, right: SnailPair) : this(parent) {
    left.parent = this
    this.left = left
    right.parent = this
    this.right = right
  }
  constructor(parent: SnailPair?, left: SnailFishNumber, right: SnailFishNumber) : this(parent) {
    this.left = left
    this.right = right
  }

  lateinit var left: SnailFishNumber
  lateinit var right: SnailFishNumber

  private val isExploitable
    get() = left is SnailNumber && right is SnailNumber
  override val magnitude: Int
    get() = 3 * left.magnitude + 2 * right.magnitude

  override fun toString(): String = "[$left,$right]"

  fun reduceOnce(): Boolean = exploit(0) || split()

  fun reduce() {
    do { val hasReduced = reduceOnce() } while (hasReduced)
  }

  private fun split(): Boolean =
    when { // Returning true breaks the recursion
      (left as? SnailNumber)?.isSplittable ?: false -> {
        left = (left as SnailNumber).split(this)
        true
      }
      (left as? SnailPair)?.split() ?: false -> true
      (right as? SnailNumber)?.isSplittable ?: false -> {
        right = (right as SnailNumber).split(this)
        true
      }
      else -> (right as? SnailPair)?.split() ?: false
    }

  private fun exploit(level: Int): Boolean {
    fun sumToLeft(amount: Int, current: SnailPair, parent: SnailPair?) {
      if (parent == null) return
      else if (parent.left == current) sumToLeft(amount, parent, parent.parent)
      else {
        var leftNumber = parent.left
        while (leftNumber is SnailPair) leftNumber = leftNumber.right
        (leftNumber as SnailNumber).magnitude += amount
      }
    }

    fun sumToRight(amount: Int, current: SnailPair, parent: SnailPair?) {
      if (parent == null) return
      else if (parent.right == current) sumToRight(amount, parent, parent.parent)
      else {
        var rightNumber = parent.right
        while (rightNumber is SnailPair) rightNumber = rightNumber.left
        (rightNumber as SnailNumber).magnitude += amount
      }
    }

    fun exploit(pair: SnailPair) {
      sumToLeft((pair.left as SnailNumber).magnitude, pair, this)
      sumToRight((pair.right as SnailNumber).magnitude, pair, this)
    }

    return when {
      level >= 3 && (left as? SnailPair)?.isExploitable ?: false -> {
        exploit(left as SnailPair)
        left = SnailNumber(0)
        true
      }
      level >= 3 && (right as? SnailPair)?.isExploitable ?: false -> {
        exploit(right as SnailPair)
        right = SnailNumber(0)
        true
      }
      else -> (((left as? SnailPair)?.exploit(level + 1)) ?: false) || (((right as? SnailPair)?.exploit(level + 1)) ?: false)
    }
  }
}

fun parseSnailFishNumber(line: String): SnailPair {
  fun parseSnailFishNumber(input: Iterator<Char>, parent: SnailPair?): SnailFishNumber =
    when (val c = input.next()) {
      '[' -> {
        val pair = SnailPair(parent)
        pair.left = parseSnailFishNumber(input, pair)
        input.next() // ignoring comma
        pair.right = parseSnailFishNumber(input, pair)
        input.next() // consuming the closing ']'
        pair
      }
      // gotta be a digit since numbers greater than 9 are not allowed (must be split)
      else -> SnailNumber(c.digitToInt())
    }

  return parseSnailFishNumber(line.iterator(), null) as SnailPair // The root is always a Pair
}

fun sum(n1: SnailPair, n2: SnailPair): SnailPair {
  val result = SnailPair(null, n1, n2)
  result.reduce()
  return result
}

fun main() {
  File("./input/day18.txt").useLines { lines ->
    val input = lines.toList()
    println(input.map { parseSnailFishNumber(it) }.reduce(::sum).magnitude)

    println(input
      .flatMap { x -> input.filterNot { it == x }.map { y -> x to y } }
      .maxOf { (x, y) -> sum(parseSnailFishNumber(x), parseSnailFishNumber(y)).magnitude })
  }
}
