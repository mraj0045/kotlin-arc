package com.arc.kotlin.util.parser

class FloatParser : Parser<Float> {

    override fun getValue(`val`: String): Float {
        try {
            return java.lang.Float.parseFloat(`val`)
        } catch (ignored: NumberFormatException) {
        } catch (ignored: NullPointerException) {
        }

        return java.lang.Float.NaN
    }
}
