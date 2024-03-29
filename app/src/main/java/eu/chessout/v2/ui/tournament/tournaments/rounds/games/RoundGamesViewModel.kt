package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.Game
import eu.chessout.v2.util.MyFirebaseUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RoundGamesViewModel : ViewModel() {
    lateinit var clubId: String
    lateinit var tournamentId: String
    val liveGames = MutableLiveData<List<Game>>()
    var roundId: Int = -1
    var myFirebaseUtils = MyFirebaseUtils()

    var isAdmin = MutableLiveData<Boolean>(false)

    fun initialize(clubId: String, tournamentId: String, roundId: Int) {
        this.clubId = clubId
        this.tournamentId = tournamentId
        this.roundId = roundId

        initLiveGames()
        GlobalScope.launch {
            isAdmin.postValue(myFirebaseUtils.isCurrentUserAdmin(clubId))
        }
    }

    private fun initLiveGames() {
        class MyListener : MyFirebaseUtils.GamesListener {
            override fun listUpdated(games: List<Game>) {
                liveGames.value = games
            }
        }
        myFirebaseUtils.registerGamesListener(
            false, tournamentId, roundId, MyListener()
        )
    }
}
