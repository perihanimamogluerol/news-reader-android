package com.perihan.newsreaderandroid.domain.mapper

interface BaseMapper<in Input, out Output> {
    fun toDomain(input: Input): Output
}