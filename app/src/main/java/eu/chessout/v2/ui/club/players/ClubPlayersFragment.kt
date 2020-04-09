package eu.chessout.v2.ui.club.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.Player
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.club_players_fragment.*

class ClubPlayersFragment : Fragment() {


    private lateinit var viewModel: ClubPlayersViewModel

    private val myListAdapter = ClubPlayersAdapter(arrayListOf())
    private val myObserver = Observer<List<Player>> { list ->
        list?.let {
            my_recycler_view.visibility = View.VISIBLE
            myListAdapter.updateList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView = inflater.inflate(R.layout.club_players_fragment, container, false)
        val model: ClubPlayersViewModel by viewModels()
        viewModel = model

        viewModel.isAdmin.observe(viewLifecycleOwner, Observer { isAdmin ->
            run {
                if (isAdmin) {
                    fab.visibility = View.VISIBLE
                } else {
                    fab.visibility = View.GONE
                }
            }
        })
        viewModel.livePlayerList.observe(viewLifecycleOwner, myObserver)
        viewModel.initializeModel()
        myListAdapter.setFragmentManager(childFragmentManager)
        val myRecyclerView = mView.findViewById<RecyclerView>(R.id.my_recycler_view)
        myRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = myListAdapter
        }

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab.setOnClickListener {
            val clubKey = viewModel.getClubKey().value!!
            ClubPlayerCreateDialogFragment(clubKey).show(
                childFragmentManager,
                "ClubPlayerCreateDialogFragment"
            )
        }

    }
}
