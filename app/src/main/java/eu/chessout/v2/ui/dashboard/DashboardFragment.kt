package eu.chessout.v2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.v2.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList: ArrayList<DashboardModel>? = null
    private var dashboardAdapter: DashboardAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel =
//            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })*/

        recyclerView = root.findViewById(R.id.my_recycler_view)
        gridLayoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
        arrayList = setDataInList()
        dashboardAdapter = DashboardAdapter(context!!, arrayList!!)
        recyclerView?.adapter = dashboardAdapter
        return root
    }

    private fun setDataInList(): ArrayList<DashboardModel> {

        var items: ArrayList<DashboardModel> = ArrayList()
        items.add(DashboardModel(R.drawable.chess_king_v1, "King"))
        items.add(DashboardModel(R.drawable.chess_queen_v1, "Queen"))
        items.add(DashboardModel(R.drawable.chess_bishop_v1, "Bishop"))
        items.add(DashboardModel(R.drawable.chess_knight_v1, "Knight"))
        items.add(DashboardModel(R.drawable.chess_rook_v1, "Rook"))
        items.add(DashboardModel(R.drawable.chess_pawn_v1, "Pawn"))
        items.add(DashboardModel(R.drawable.chess_king_and_rook_v1, "King and rook"))

        return items
    }
}