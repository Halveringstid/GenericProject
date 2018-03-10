package me.rozkmin.generic

/**
 * Created by edawhuj on 2018-03-10.
 */

data class Position(
        val id: String = "",
        val author: String = "",
        val created_at: String = "",
        val lat: Double,
        val lon: Double,
        val message: String
)

data class Wrapper(
        val id: String = "",
        val data: Position
)