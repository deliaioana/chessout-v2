package eu.chessout.v2.ui.club.joinclub

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.Club

class JoinClubModel() : ViewModel() {

    val clubList = MutableLiveData<List<Club>>()

    fun initializeModel() {

    }

}