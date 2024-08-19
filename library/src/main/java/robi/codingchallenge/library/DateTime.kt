package robi.codingchallenge.library

import android.annotation.SuppressLint
import java.time.Instant
import kotlin.time.Duration

object DateTime {
    @SuppressLint("NewApi")
    fun duration(time: Long): String {
        val nowMillis = System.currentTimeMillis()
        val differenceMillis = nowMillis - time
        val seconds = differenceMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            differenceMillis < 0 -> "In the future" // Handle future timestamps
            days > 0 -> "${days} day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "${hours} hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "${minutes} minute${if (minutes > 1) "s" else ""} ago"
            else -> "${seconds} second${if (seconds > 1) "s" else ""} ago"
        }
    }
}