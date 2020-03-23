package eu.chessout.v2.ui.tournament.tournaments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import eu.chessout.v2.R

class TournamentDashboardFragment : Fragment() {

    private lateinit var viewModel: TournamentDashboardViewModel
    private val args: TournamentDashboardFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tournament_dashboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val model: TournamentDashboardViewModel by viewModels()
        viewModel = model
        viewModel.initializeModel(args.tournamentId, args.clubId)
    }

}
