package eu.chessout.v2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.v2.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private var recyclerView: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayList: ArrayList<DashboardModel>? = null
    private var dashboardAdapter: DashboardAdapter? = null

    private lateinit var myViewModel: MyViewModel
    private val myListAdapter = MyAdapter(arrayListOf())

    private val myObserver = Observer<List<DashboardModel>> { list -> }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        myViewModel.dashboardModelList.observe(this, myObserver)
        myViewModel.getInitialList()

        //recyclerView =
    }
}