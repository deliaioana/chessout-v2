package eu.chessout.v2.ui.club.players

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import eu.chessout.shared.Constants
import eu.chessout.shared.model.DefaultClub
import eu.chessout.shared.model.Player
import eu.chessout.v2.util.MyFirebaseUtils

class ClubPlayersViewModel : ViewModel() {
    var clubKey = MutableLiveData<String>().apply { value = "" }
    private lateinit var myFirebaseUtils: MyFirebaseUtils
    var isAdmin = MutableLiveData<Boolean>(false)
    val livePlayerList = MutableLiveData<List<Player>>()

    fun getClubKey(): LiveData<String> {
        return clubKey
    }

    fun initializeModel() {
        myFirebaseUtils = MyFirebaseUtils()

        class ClubListener : MyFirebaseUtils.DefaultClubListener {
            override fun onDefaultClubValue(defaultClub: DefaultClub) {
                processDefaultClub(defaultClub)
                initializeList()
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

    private fun initializeList() {
        val playersLoc: String = Constants.LOCATION_CLUB_PLAYERS
            .replace(Constants.CLUB_KEY, clubKey.value!!)
        val playersRef =
            FirebaseDatabase.getInstance().getReference(playersLoc)
        val eventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // nothing to do on cancel
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val players: ArrayList<Player> = ArrayList()
                for (item in dataSnapshot.children) {
                    val player = item.getValue(Player::class.java)!!
                    players.add(player)
                }
                livePlayerList.value = players
            }
        }
        playersRef.addValueEventListener(eventListener)
    }
}
