package com.ntduc.fileutils

import java.security.SecureRandom
import java.util.*
import kotlin.experimental.and
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.ln

object NanoIdUtils {
  private val DEFAULT_NUMBER_GENERATOR = SecureRandom()
  private val DEFAULT_ALPHABET =
    "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
  private const val DEFAULT_SIZE = 21
  
  fun randomNanoId(
    random: Random = DEFAULT_NUMBER_GENERATOR,
    alphabet: CharArray = DEFAULT_ALPHABET,
    size: Int = DEFAULT_SIZE
  ): String {
    require(!(alphabet.isEmpty() || alphabet.size >= 256)) { "alphabet must contain between 1 and 255 symbols." }
    require(size > 0) { "size must be greater than zero." }
    val mask =
      (2 shl floor(ln((alphabet.size - 1).toDouble()) / ln(2.0)).toInt()) - 1
    val step = ceil(1.6 * mask * size / alphabet.size).toInt()
    val idBuilder = StringBuilder()
    while (true) {
      val bytes = ByteArray(step)
      random.nextBytes(bytes)
      for (i in 0 until step) {
        val alphabetIndex: Byte = bytes[i] and mask.toByte()
        if (alphabetIndex < alphabet.size) {
          idBuilder.append(alphabet[alphabetIndex.toInt()])
          if (idBuilder.length == size) {
            return idBuilder.toString()
          }
        }
      }
    }
  }
}