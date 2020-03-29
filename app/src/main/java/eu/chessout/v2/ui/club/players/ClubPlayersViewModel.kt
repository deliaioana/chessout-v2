package eu.chessout.v2.ui.club.players

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.DefaultClub
import eu.chessout.v2.util.MyFirebaseUtils

class ClubPlayersViewModel : ViewModel() {
    private var clubKey = MutableLiveData<String>().apply { value = "" }
    private lateinit var myFirebaseUtils: MyFirebaseUtils
    var isAdmin = MutableLiveData<Boolean>(false)

    fun initializeModel() {
        myFirebaseUtils = MyFirebaseUtils()

        class ClubListener : MyFirebaseUtils.DefaultClubListener {
            override fun onDefaultClubValue(defaultClub: DefaultClub) {
                processDefaultClub(defaultClub)
            }
        }
        myFirebaseUtils.getDefaultClubSingleValueListener(ClubListener())
    }

    private fun processDefaultClub(defaultClub: DefaultClub) {
        clubKey.value = defaultClub.clubKey

        class IsAdminListener : MyFirebaseUtils.IsAdminListener {
            override fun onIsAdmin(returnAdmin: Boolean) {
                isAdmin.value = returnAdmin
            }
        }

        myFirebaseUtils.isCurrentUserAdmin(defaultClub.clubKey, IsAdminListener())
    }
}
