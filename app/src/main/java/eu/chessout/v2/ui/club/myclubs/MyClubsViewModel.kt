package eu.chessout.v2.ui.club.myclubs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.DefaultClub
import eu.chessout.v2.util.MyFirebaseUtils

class MyClubsViewModel : ViewModel() {

    val liveDefaultClubId = MutableLiveData("no-default-club")


    fun initializeModel() {
        class DefaultClubListener : MyFirebaseUtils.DefaultClubListener {
            override fun onDefaultClubValue(defaultClub: DefaultClub) {
                liveDefaultClubId.value = defaultClub.clubKey
            }
        }

        MyFirebaseUtils().getDefaultClubListener(DefaultClubListener())
    }

}
