package eu.chessout.v2.ui.club.players

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.FirebaseDatabase
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Player
import eu.chessout.v2.R

class ClubPlayerCreateDialogFragment(var mClubKey: String) : DialogFragment() {

    private lateinit var mView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        mView = inflater.inflate(R.layout.club_player_create_dialog, null)
        builder.setView(mView)

        builder.setNegativeButton("Cancel") { _, _ ->
            dismiss()
        }

        builder.setPositiveButton("Create player") { _, _ ->
            persistPlayer()
        }

        return builder.create()
    }


    private fun persistPlayer() {
        val player = buildPlayer()

        val database =
            FirebaseDatabase.getInstance()

        val playersLoc: String = Constants.LOCATION_CLUB_PLAYERS
            .replace(Constants.CLUB_KEY, mClubKey)
        val playersRef =
            database.getReference(playersLoc)
        val playerRef = playersRef.push()
        val playerKey = playerRef.key
        player.playerKey = playerKey
        playerRef.setValue(player)
    }

    private fun buildPlayer(): Player {
        val name =
            (mView.findViewById<View>(R.id.profileName) as EditText).text
                .toString()
        val email =
            (mView.findViewById<View>(R.id.email) as EditText).text.toString()
        val eloString =
            (mView.findViewById<View>(R.id.elo) as EditText).text.toString()
        val elo = Integer.valueOf(eloString)
        val clubEloString =
            (mView.findViewById<View>(R.id.clubElo) as EditText).text.toString()
        val clubElo = Integer.valueOf(eloString)
        return Player(name, email, mClubKey, elo, clubElo)
    }
}