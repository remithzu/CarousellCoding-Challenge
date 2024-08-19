package robi.codingchallenge.library

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale
import java.time.Duration
import java.time.format.DateTimeFormatter

object DateTime {
    @SuppressLint("NewApi")
    fun duration(timeMillis: Long): String {
        val currentDateTime = LocalDateTime.now()
        val nowMillis = currentDateTime.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()
        val diff = nowMillis - timeMillis
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(diff)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
        val diffDate = LocalDateTime.parse(formattedDate, formatter)
        val duration = Duration.between(diffDate, currentDateTime)

        val days = duration.toDays()
        val hours = duration.minusDays(days).toHours()
        val minutes = duration.minusDays(days).minusHours(hours).toMinutes()
        val seconds = duration.minusDays(days).minusHours(hours).minusMinutes(minutes).seconds
        val years = days / 365
        val months = (days % 365) / 30
        val weeks = (days % 365) % 30 / 7

        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""} ago"
            months > 0 -> "$months month${if (months > 1) "s" else ""} ago"
            weeks > 0 -> "$weeks week${if (weeks > 1) "s" else ""} ago"
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "$seconds second${if (seconds > 1) "s" else ""} ago"
        }
    }
}