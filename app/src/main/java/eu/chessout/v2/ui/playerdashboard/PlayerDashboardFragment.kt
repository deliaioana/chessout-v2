package eu.chessout.v2.ui.playerdashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private val MY_REQUEST_CODE = 100

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
            if (askForPermissions()) {
                Log.d(Constants.LOG_TAG, "Permission ok")
            } else {
                Log.d(Constants.LOG_TAG, "Permissions not ok")
            }
        }

        viewModel.initModel()
    }

    fun isPermissionsAllowed(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return permission == PackageManager.PERMISSION_GRANTED
    }

    fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    requireContext(), "Permission denied by rationale",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requireActivity().requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_REQUEST_CODE
                    )
                }
            }
            return false
        }
        return true
    }
}
