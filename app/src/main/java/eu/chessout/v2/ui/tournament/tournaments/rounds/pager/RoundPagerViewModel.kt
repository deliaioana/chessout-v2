package eu.chessout.v2.ui.tournament.tournaments.rounds.pager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoundPagerViewModel : ViewModel() {
    val position = MutableLiveData<Int>()
    fun setPosition(position: Int) {
        this.position.value = position
    }
}