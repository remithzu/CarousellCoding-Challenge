package robi.codingchallenge.networks.data
import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep
data class News(
    @SerializedName("banner_url")
    val bannerUrl: String, // https://storage.googleapis.com/carousell-interview-assets/android/images/carousell-siu-rui-ceo-tia-sg-2018.jpg
    @SerializedName("description")
    val description: String, // Due to launch next month in Singapore, CarouPay will allow buyers and sellers to complete transactions without leaving the Carousell app, rather than having to rely on third-party platforms or doing meet-ups to hand over cash. CarouPay will be a digital wallet within the Carousell app. "More than half of our sellers will end up buying items as well, so maybe it makes sense to have that money in the wallet for purchases" - Quek tells Tech in Asia.
    @SerializedName("id")
    val id: String, // 121
    @SerializedName("rank")
    val rank: Int, // 2
    @SerializedName("time_created")
    val timeCreated: Int, // 1532853058
    @SerializedName("title")
    val title: String // Carousell is launching its own digital wallet to improve payments for its users
)