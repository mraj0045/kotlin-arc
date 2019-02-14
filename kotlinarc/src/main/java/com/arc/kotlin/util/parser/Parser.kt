package com.arc.kotlin.util.parser

interface Parser<T> {
    fun getValue(`val`: String): T
}
