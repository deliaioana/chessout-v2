package eu.chessout.v2.ui.playerdashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import eu.chessout.shared.Constants
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.player_dashboard_fragment.*

class PlayerDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerDashboardFragment()
    }

    lateinit var clubId: String
    lateinit var playerId: String
    private val viewModel: PlayerDashboardViewModel by viewModels()
    private val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.clubId = requireArguments().getString("clubId")!!
            this.playerId = requireArguments().getString("playerId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_dashboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profilePicture.setOnClickListener {
            Log.d(Constants.LOG_TAG, "ProfilePicture clicked")
        }

        viewModel.initModel()
    }
}
