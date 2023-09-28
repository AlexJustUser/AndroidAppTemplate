package com.softteco.template.utils

@OptIn(ExperimentalUnsignedTypes::class)
fun characteristicByteConversation(bytes: ByteArray, startIndex: Int, endIndex: Int): Double {
    val array = bytes.copyOfRange(0, 2).toUByteArray()
    var result = 0
    for (i in array.indices) {
        result = result or (array[i].toInt() shl 8 * i)
    }
    return result.toDouble()
}
