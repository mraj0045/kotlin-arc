package com.arc.kotlin.util.extensions

/**Returns whether the object is null or not*/
fun Any?.isNull(): Boolean = this == null

/**Returns whether the object is not null or not*/
fun Any?.isNotNull(): Boolean = !isNull()