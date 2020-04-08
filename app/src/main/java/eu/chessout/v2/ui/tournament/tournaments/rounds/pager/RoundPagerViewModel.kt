package eu.chessout.v2.ui.tournament.tournaments.rounds.pager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.v2.util.MyFirebaseUtils

class RoundPagerViewModel : ViewModel() {
    val position = MutableLiveData<Int>()
    val visibleRoundsCount = MutableLiveData(1)
    val myFirebaseUtils = MyFirebaseUtils()
    lateinit var tournamentId: String
    var totalRounds = -1

    fun setPosition(position: Int) {
        this.position.value = position
    }

    fun initializeModel(tournamentId: String, totalRounds: Int) {
        this.tournamentId = tournamentId
        this.totalRounds = totalRounds

        class MyListener : MyFirebaseUtils.LongListener {
            override fun valueUpdated(value: Long) {
                visibleRoundsCount.value = value.toInt()
            }
        }
        myFirebaseUtils.registerCompletedRoundsListener(
            false, this.tournamentId, totalRounds.toLong(), MyListener()
        )
    }
}