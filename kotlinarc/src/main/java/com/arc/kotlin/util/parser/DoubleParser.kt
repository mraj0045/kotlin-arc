package com.arc.kotlin.util.parser

class DoubleParser : Parser<Double> {
    override fun getValue(`val`: String): Double {
        try {
            return java.lang.Double.parseDouble(`val`)
        } catch (ignored: NumberFormatException) {
        } catch (ignored: NullPointerException) {
        }
        return java.lang.Double.NaN
    }

}
