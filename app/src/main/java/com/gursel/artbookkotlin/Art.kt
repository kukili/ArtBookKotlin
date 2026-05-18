package com.gursel.artbookkotlin

data class Art(
    val id: Int,
    val name: String,
    val painter: String?,
    val year: String?,
    val image: ByteArray?
)

