package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class RoundSetGameResultDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Update result")

        builder.setNegativeButton("Cancel") { _, _ -> dismiss() }

    }
}