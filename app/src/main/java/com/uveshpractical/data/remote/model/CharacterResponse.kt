package com.uveshpractical.data.model

data class CharacterResponse(
    val info: Info,
    val results: List<CharacterDetailDto>
) {
    data class Info(
        val count: Int,
        val next: String,
        val pages: Int,
        val prev: Any
    )
}
