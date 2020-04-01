package eu.chessout.v2.ui.tournament.tournaments.rounds.players

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.Player
import eu.chessout.v2.util.MyFirebaseUtils

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

        tournamentPlayers = myFirebaseUtils.waitGetTournamentPlayers(tournamentId)
    }

    fun initMissingPlayers() {

    }


}