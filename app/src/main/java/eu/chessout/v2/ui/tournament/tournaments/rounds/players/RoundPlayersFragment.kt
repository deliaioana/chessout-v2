package eu.chessout.v2.ui.tournament.tournaments.rounds.players

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.chessout.shared.Constants
import eu.chessout.v2.R

class RoundPlayersFragment : Fragment() {

    companion object {
        fun newInstance(clubId: String, tournamentId: String, roundId: Int) =
            RoundPlayersFragment().apply {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clubId = requireArguments().getString("clubId")!!
            tournamentId = requireArguments().getString("tournamentId")!!
            roundId = requireArguments().getInt("roundId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(Constants.LOG_TAG, "Debug round: $roundId")
        return inflater.inflate(R.layout.round_players_fragment, container, false)
    }


}