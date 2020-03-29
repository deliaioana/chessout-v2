package eu.chessout.v2.ui.club.players

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import eu.chessout.v2.R

class ClubPlayerCreateDialogFragment(var mClubKey: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val mView = inflater.inflate(R.layout.club_player_create_dialog, null)
        builder.setView(mView)

        builder.setNegativeButton("Cancel") { _, _ ->
            dismiss()
        }

        return builder.create()
    }
}