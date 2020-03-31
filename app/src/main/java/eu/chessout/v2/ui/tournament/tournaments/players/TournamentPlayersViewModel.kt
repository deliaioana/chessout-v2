package eu.chessout.v2.ui.tournament.tournaments.players

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.Player
import eu.chessout.v2.util.MyFirebaseUtils

class TournamentPlayersViewModel : ViewModel() {
    private var clubId = MutableLiveData<String>().apply { value = "" }
    private var tournamentId = MutableLiveData<String>().apply { value = "" }
    var isAdmin = MutableLiveData<Boolean>(false)
    val livePlayerList = MutableLiveData<List<Player>>()
    val liveClubPlayerList = MutableLiveData<List<Player>>()
    val missingPlayers = MutableLiveData<List<Player>>()

    private lateinit var myFirebaseUtils: MyFirebaseUtils

    fun initializeModel(clubId: String, tournamentId: String) {
        this.clubId.value = clubId
        this.tournamentId.value = tournamentId
        myFirebaseUtils = MyFirebaseUtils()

        initIsAdmin()
        initPlayers()
    }


    private fun initIsAdmin() {
        class IsAdminListener : MyFirebaseUtils.IsAdminListener {
            override fun onIsAdmin(returnAdmin: Boolean) {
                isAdmin.value = returnAdmin
            }
        }
        myFirebaseUtils.isCurrentUserAdmin(clubId.value, IsAdminListener())
    }

    fun getClubId(): String {
        return clubId!!.value!!
    }

    fun getTournamentId(): String {
        return tournamentId!!.value!!
    }

    fun getMissingPlayers(): List<Player> {
        return missingPlayers.value!!
    }

    private fun initPlayers() {
        initClubPlayersList()
    }

    private fun initClubPlayersList() {
        class PlayersListener : MyFirebaseUtils.PlayersListener {
            override fun listUpdated(players: List<Player>) {
                liveClubPlayerList.value = players
                updateMissingPlayers()
            }
        }
        myFirebaseUtils.getClubPlayers(clubId.value!!, true, PlayersListener())
    }

    private fun updateMissingPlayers() {
        val missingList: List<Player> = liveClubPlayerList.value!!
        missingPlayers.value = missingList
    }
}
