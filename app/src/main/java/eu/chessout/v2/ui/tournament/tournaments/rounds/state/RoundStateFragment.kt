package eu.chessout.v2.ui.tournament.tournaments.rounds.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.round_state_fragment.*

class RoundStateFragment(
    private val clubId: String,
    private val tournamentId: String,
    private val roundId: Int
) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.round_state_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textView.text = "Round number $roundId"
    }
}