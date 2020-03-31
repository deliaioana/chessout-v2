package eu.chessout.v2.ui.tournament.tournaments.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.Player
import eu.chessout.v2.R
import eu.chessout.v2.ui.tournament.tournaments.addplayers.TournamentAddPlayersDialog
import kotlinx.android.synthetic.main.tournament_players_fragment.*

class TournamentPlayersFragment() : Fragment() {

    private lateinit var viewModel: TournamentPlayersViewModel
    private val args: TournamentPlayersFragmentArgs by navArgs()
    private val myListAdapter = TournamentPlayersAdapter(arrayListOf())
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
        val mView = inflater.inflate(R.layout.tournament_players_fragment, container, false)

        val model: TournamentPlayersViewModel by viewModels()
        viewModel = model

        viewModel.isAdmin.observe(viewLifecycleOwner, Observer { isAdmin ->
            if (isAdmin) {
                fab.visibility = View.VISIBLE
            } else {
                fab.visibility = View.GONE
            }
        })
        viewModel.livePlayerList.observe(viewLifecycleOwner, myObserver)
        viewModel.initializeModel(args.clubId, args.tournamentId)

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
            TournamentAddPlayersDialog(
                viewModel.getClubId(),
                viewModel.getTournamentId(),
                viewModel.getMissingPlayers()
            ).show(childFragmentManager, "TournamentAddPlayersFragment")
        }
    }

}
