package eu.chessout.v2.ui.club.joinclub

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.shared.model.Club

class JoinClubModel() : ViewModel() {

    val liveClubs = MutableLiveData<List<Club>>()

    fun initializeModel() {
        val clubs: ArrayList<Club> = ArrayList()

        clubs.add(
            Club(
                "long name",
                "short name",
                "email",
                "coutry",
                "city",
                "home page",
                "description"
            )
        )

        liveClubs.value = clubs

    }

}