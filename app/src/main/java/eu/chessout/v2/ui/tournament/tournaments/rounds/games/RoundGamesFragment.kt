package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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

    private lateinit var viewModel: RoundGamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.round_games_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RoundGamesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
