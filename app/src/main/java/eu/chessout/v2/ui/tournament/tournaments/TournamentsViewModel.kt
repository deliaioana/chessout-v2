package eu.chessout.v2.ui.tournament.tournaments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.DefaultClub
import eu.chessout.v2.util.MyFirebaseUtils


class TournamentsViewModel : ViewModel() {
    private var clubKey = MutableLiveData<String>().apply { value = "" }
    private lateinit var myFirebaseUtils: MyFirebaseUtils

    fun getClubKey(): LiveData<String> {
        return clubKey
    }

    fun initializeModel() {
        myFirebaseUtils = MyFirebaseUtils()

        class ClubListener : MyFirebaseUtils.DefaultClubListener {
            override fun onDefaultClubValue(defaultClub: DefaultClub) {
                processDefaultClub(defaultClub)
            }
        }
        myFirebaseUtils.getDefaultClub(ClubListener())

    }

    private fun processDefaultClub(defaultClub: DefaultClub) {
        clubKey.value = defaultClub.clubKey
    }
}
