package net.lyxnx.carcheck.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    const val DATE_PATTERN = "dd MMMM yyyy HH:mm"

    fun formatDate(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN))
    }
}