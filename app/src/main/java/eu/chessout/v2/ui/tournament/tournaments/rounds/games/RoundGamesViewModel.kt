package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import androidx.lifecycle.ViewModel

class RoundGamesViewModel : ViewModel() {
    lateinit var clubId: String
    lateinit var tournamentId: String
    var roundId: Int = -1

    fun initialize(clubId: String, tournamentId: String, roundId: Int) {
        this.clubId = clubId
        this.tournamentId = tournamentId
        this.roundId = roundId


    }
}
