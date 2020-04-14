package eu.chessout.v2.ui.playerdashboard

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.Constants

class PlayerDashboardViewModel : ViewModel() {
    private lateinit var clubId: String
    private lateinit var playerId: String
    val defaultPictureUri = MutableLiveData<String?>()

    fun initModel(clubId: String, playerId: String) {
        this.clubId = clubId
        this.playerId = playerId
    }

    fun setDefaultPicture(uri: Uri) {
        Log.d(Constants.LOG_TAG, "Time to use glide to show the image: $uri")
        this.defaultPictureUri.value = uri.toString()
    }
}
