package com.perihan.newsreaderandroid.core.common

interface BaseMapper<I, D, L> {
    fun toDomain(input: I): D
    fun toLocalData(input: D): L? = null
    fun toLocalDomain(input: L): D? = null
}