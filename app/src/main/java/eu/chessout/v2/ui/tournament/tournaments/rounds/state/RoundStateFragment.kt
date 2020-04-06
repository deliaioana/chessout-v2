package eu.chessout.v2.ui.tournament.tournaments.rounds.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.chessout.shared.model.Player
import eu.chessout.v2.R
import eu.chessout.v2.ui.tournament.tournaments.rounds.players.RoundAbsentPlayersFragment
import kotlinx.android.synthetic.main.round_state_fragment.*

class RoundStateFragment(
    private val clubId: String,
    private val tournamentId: String,
    private val roundId: Int
) : Fragment() {

    lateinit var mView: View
    private lateinit var absentPlayersFragment: RoundAbsentPlayersFragment
    private lateinit var viewModel: RoundStateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.round_state_fragment, container, false)

        absentPlayersFragment = RoundAbsentPlayersFragment.newInstance(
            clubId, tournamentId, roundId
        )
        val transaction = childFragmentManager.beginTransaction();
        transaction.replace(R.id.stateContainerView, absentPlayersFragment)
        transaction.commit()

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textView.text = "Round number $roundId"
        val model: RoundStateViewModel by viewModels()
        viewModel = model
        viewModel.initialize(tournamentId, roundId)
    }

    fun getPresentPlayers(): List<Player> {
        return absentPlayersFragment.getPresentPlayers()
    }

}