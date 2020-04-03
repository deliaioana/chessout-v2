package eu.chessout.v2.ui.tournament.tournaments.rounds.players

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.Player
import eu.chessout.v2.util.MyFirebaseUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RoundAbsentPlayersViewModel : ViewModel() {
    lateinit var clubId: String
    lateinit var tournamentId: String
    var roundId: Int = -1
    val liveMissingPlayers = MutableLiveData<List<Player>>()
    var tournamentPlayers: List<Player> = ArrayList<Player>()
    var myFirebaseUtils = MyFirebaseUtils()

    fun initialize(clubId: String, tournamentId: String) {
        this.clubId = clubId
        this.tournamentId = tournamentId

        GlobalScope.async {
            tournamentPlayers = myFirebaseUtils.suspendGetTournamentPlayers(tournamentId)
        }
    }

    fun getPresentPlayers(): List<Player> {
        val map = LinkedHashMap<String, Player>()
        val missingSet = HashSet<String>()
        missingSet.addAll(liveMissingPlayers.value!!.map { it.playerKey })
        val presentPlayers = ArrayList<Player>()
        tournamentPlayers.forEach {
            if (!missingSet.contains(it.playerKey)) {
                presentPlayers.add(it)
            }
        }
        return presentPlayers
    }
}