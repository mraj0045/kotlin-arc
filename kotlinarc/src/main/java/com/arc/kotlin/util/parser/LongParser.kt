package com.arc.kotlin.util.parser

class LongParser : Parser<Long> {

    override fun getValue(`val`: String): Long {
        try {
            return java.lang.Long.parseLong(`val`)
        } catch (ignored: NumberFormatException) {
        } catch (ignored: NullPointerException) {
        }
        return 0L
    }
}
