package eu.chessout.v2.ui.playerdashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.chessout.shared.Constants
import eu.chessout.v2.R

class PlayerDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerDashboardFragment()
    }

    private val viewModel: PlayerDashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Log.d(Constants.LOG_TAG, "club id = ${requireArguments().getString("clubId")}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_dashboard_fragment, container, false)
    }

}
