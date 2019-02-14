package com.arc.kotlin.util.parser

class IntParser : Parser<Int> {

    override fun getValue(`val`: String): Int {
        try {
            return Integer.parseInt(`val`)
        } catch (ignored: NumberFormatException) {
        } catch (ignored: NullPointerException) {
        }

        return 0
    }
}
