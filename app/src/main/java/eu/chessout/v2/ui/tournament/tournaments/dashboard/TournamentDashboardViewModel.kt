package eu.chessout.v2.ui.tournament.tournaments.dashboard

import androidx.lifecycle.ViewModel

class TournamentDashboardViewModel : ViewModel() {
    lateinit var tournamentId: String
    lateinit var clubId: String

    fun initializeModel(tournamentId: String, clubId: String) {
        this.tournamentId = tournamentId
        this.clubId = clubId
    }
}
