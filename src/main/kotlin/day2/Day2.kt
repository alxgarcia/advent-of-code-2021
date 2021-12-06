package day2

import java.io.File

sealed interface SubmarineActions<out S : SubmarineActions<S>> {
  fun moveForward(delta: Int): S
  fun goUp(delta: Int): S
  fun goDown(delta: Int): S = goUp(-delta)
}

data class Submarine1(val horizontal: Int, val depth: Int): SubmarineActions<Submarine1> {
  companion object {
    val fromStart: Submarine1
      get() = Submarine1(0, 0)
  }
  override fun moveForward(delta: Int) = this.copy(horizontal = horizontal + delta)
  override fun goUp(delta: Int) = this.copy(depth = depth - delta)
}

data class Submarine2(val horizontal: Int, val depth: Int, val aim: Int): SubmarineActions<Submarine2> {
  companion object {
    val fromStart: Submarine2
      get() = Submarine2(0, 0, 0)
  }
  override fun moveForward(delta: Int) = this.copy(horizontal = horizontal + delta, depth = depth + aim * delta)
  override fun goUp(delta: Int) = this.copy(aim = aim - delta)
}

fun <S : SubmarineActions<S>> processInstruction(current: S, instruction: String): S {
  val (word, number) = instruction.split(" ")
  val delta = number.toInt()
  return when (word) {
    "forward" -> current.moveForward(delta)
    "up" -> current.goUp(delta)
    "down" -> current.goDown(delta)
    else -> current // ignore unrecognized command
  }
}

fun computeFinalHorizontalPositionAndDepthProduct(instructions: List<String>): Int {
  val s = instructions.fold(Submarine1.fromStart, ::processInstruction)
  return s.horizontal * s.depth
}

fun computeFinalHorizontalPositionAndDepthProduct2(instructions: List<String>): Int {
  val s = instructions.fold(Submarine2.fromStart, ::processInstruction)
  return s.horizontal * s.depth
}

fun main() {
  File("./input/day2.txt").useLines { lines ->
    val instructions = lines.toList()
    println("First value: ${computeFinalHorizontalPositionAndDepthProduct(instructions)}")
    println("Second value: ${computeFinalHorizontalPositionAndDepthProduct2(instructions)}")
  }
}