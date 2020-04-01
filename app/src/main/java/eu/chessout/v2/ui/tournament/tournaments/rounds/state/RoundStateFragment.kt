package eu.chessout.v2.ui.tournament.tournaments.rounds.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.chessout.v2.R
import eu.chessout.v2.ui.tournament.tournaments.rounds.players.RoundPlayersFragment
import kotlinx.android.synthetic.main.round_state_fragment.*

class RoundStateFragment(
    private val clubId: String,
    private val tournamentId: String,
    private val roundId: Int
) : Fragment() {

    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.round_state_fragment, container, false)

        val roundPlayersFragment = RoundPlayersFragment.newInstance(
            clubId, tournamentId, roundId
        )
        val transaction = childFragmentManager.beginTransaction();
        transaction.replace(R.id.stateContainerView, roundPlayersFragment)
        transaction.commit()

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textView.text = "Round number $roundId"
    }


}