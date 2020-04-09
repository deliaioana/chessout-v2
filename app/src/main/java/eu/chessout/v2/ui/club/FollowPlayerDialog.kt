package eu.chessout.v2.ui.club

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Player
import eu.chessout.shared.model.User

class FollowPlayerDialog(val player: Player) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Would you like to follow ${player.name} ?")
        builder.setNegativeButton("No") { _, _ -> dismiss() }
        builder.setPositiveButton("Yes") { _, _ -> persistNewPlayerToFollow() }
        return builder.create()
    }

    private fun persistNewPlayerToFollow() {
        val userKey =
            FirebaseAuth.getInstance().currentUser!!.uid
        val followLoc: String = Constants.LOCATION_MY_FOLLOWED_PLAYERS_BY_PLAYER
            .replace(Constants.USER_KEY, userKey)
            .replace(Constants.PLAYER_KEY, player.playerKey)
        val followRef =
            FirebaseDatabase.getInstance().getReference(followLoc)
        followRef.setValue(player)

        //set the global followers
        val name =
            FirebaseAuth.getInstance().currentUser!!.displayName
        val email =
            FirebaseAuth.getInstance().currentUser!!.email
        val user = User(name, email, userKey)
        val globalFollowerLoc: String = Constants.LOCATION_GLOBAL_FOLLOWER_BY_PLAYER
            .replace(Constants.PLAYER_KEY, player.playerKey)
            .replace(Constants.USER_KEY, userKey)
        val globalFollowerRef =
            FirebaseDatabase.getInstance()
                .getReference(globalFollowerLoc)
        globalFollowerRef.setValue(user)

        //dismiss the dialog
        dismiss()
    }
}