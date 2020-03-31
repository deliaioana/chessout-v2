package eu.chessout.v2.ui.tournament.tournaments.addplayers

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class TournamentAddPlayersFragment(val clubId: String, val tournamentId: String) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Fire Missiles?")
        builder.setNegativeButton("Cancel") { _, _ ->
            dismiss()
        }
        builder.setPositiveButton("Fire") { _, _ ->
            dismiss()
        }
        return builder.create()
    }
}