package com.boscatov.schedulercw.util

import java.lang.StringBuilder

/**
 * Created by boscatov on 13.04.2019.
 */

fun secondsToFormatted(seconds: Long): String {
    val builder = StringBuilder()
    // seconds
    val sec = seconds % 60
    val min = seconds / 60 % 60
    val hour = seconds / 3600
    if (hour > 0) {
        if (hour < 10) {
            builder.append("0")
        }
        builder.append("$hour:")
    }

    if (min > 0) {
        if (min < 10) {
            builder.append("0")
        }
        builder.append("$min:")
    }

    if (sec >= 0) {
        if (sec < 10) {
            builder.append("0")
        }
        builder.append("$sec")
    }

    return builder.toString()
}