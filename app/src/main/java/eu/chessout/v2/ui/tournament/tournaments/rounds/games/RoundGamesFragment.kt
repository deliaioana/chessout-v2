package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        viewModel.initialize(clubId, tournamentId, roundId)
        return mView
    }

}
