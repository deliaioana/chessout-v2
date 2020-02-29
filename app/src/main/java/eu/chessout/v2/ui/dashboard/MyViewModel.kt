package eu.chessout.v2.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.chessout.v2.R

class MyViewModel() : ViewModel() {


    val dashboardModelList = MutableLiveData<List<DashboardModel>>()

    fun getInitialList() {
        var items: ArrayList<DashboardModel> = ArrayList()
        items.add(DashboardModel(R.drawable.chess_king_v1, "King"))
        items.add(DashboardModel(R.drawable.chess_queen_v1, "Queen"))
        items.add(DashboardModel(R.drawable.chess_bishop_v1, "Bishop"))
        items.add(DashboardModel(R.drawable.chess_knight_v1, "Knight"))
        items.add(DashboardModel(R.drawable.chess_rook_v1, "Rook"))
        items.add(DashboardModel(R.drawable.chess_pawn_v1, "Pawn"))
        items.add(DashboardModel(R.drawable.chess_king_and_rook_v1, "King and rook"))

        dashboardModelList.value = items
    }

}