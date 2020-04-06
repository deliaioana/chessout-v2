package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.Game
import eu.chessout.v2.R

class RoundGamesFragment : Fragment() {

    companion object {
        fun newInstance(clubId: String, tournamentId: String, roundId: Int) =
            RoundGamesFragment().apply {
                arguments = Bundle().apply {
                    putString("clubId", clubId)
                    putString("tournamentId", tournamentId)
                    putInt("roundId", roundId)
                }
            }
    }

    lateinit var clubId: String
    lateinit var tournamentId: String
    var roundId: Int = -1
    private val viewModel: RoundGamesViewModel by viewModels()
    lateinit var mView: View
    private val myListAdapter = RoundGamesAdapter(arrayListOf())
    private val myObserver = Observer<List<Game>> { list ->
        list?.let {
            myListAdapter.updateList(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clubId = requireArguments().getString("clubId")!!
            tournamentId = requireArguments().getString("tournamentId")!!
            roundId = requireArguments().getInt("roundId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.round_games_fragment, container, false)
        viewModel.liveGames.observe(viewLifecycleOwner, myObserver)
        viewModel.initialize(clubId, tournamentId, roundId)
        val myRecyclerView = mView.findViewById<RecyclerView>(R.id.my_recycler_view)
        myRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = myListAdapter
        }
        return mView
    }

}
