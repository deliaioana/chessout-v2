package eu.chessout.v2.ui.tournament.tournaments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import eu.chessout.v2.R

class TournamentCreateDialogFragment(var mClubKey: String) : DialogFragment() {

    private lateinit var mView: View
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        mView = inflater.inflate(R.layout.fragment_tournament_create_dialog, null)
        builder.setView(mView)

        val numberPicker =
            mView.findViewById<View>(R.id.tournamentTotalRounds) as NumberPicker
        numberPicker.minValue = 1
        numberPicker.maxValue = 20
        numberPicker.value = 7


        builder.setNegativeButton("Cancel") { _, _ ->
            dismiss()
        }

        builder.setPositiveButton("Create tournament") { _, _ ->
            createTournament()
        }

        return builder.create()
    }

    private fun createTournament() {
        val sb = StringBuilder()

        val tournamentName =
            (mView.findViewById<View>(R.id.tournamentName) as EditText).text
                .toString()
        if (tournamentName == null || tournamentName.isEmpty()) {
            sb.append("Tournament name is empty\n")
        }

        val tournamentDescription =
            (mView.findViewById<View>(R.id.tournamentDescription) as EditText).text
                .toString()
        if (tournamentDescription == null || tournamentDescription.isEmpty()) {
            sb.append("Tournament description is empty\n")
        }

        val tournamentLocation =
            (mView.findViewById<View>(R.id.tournamentLocation) as EditText).text
                .toString()
        if (tournamentLocation == null || tournamentLocation.isEmpty()) {
            sb.append("Tournament location is empty\n")
        }

//                String firstNumber = ((EditText) mView.findViewById(R.id.tournamentFirstTableNumber)).getText().toString();
//                if (firstNumber == null || firstNumber.isEmpty()){
//                    sb.append("Tournament first table number is empty\n");
//                }

        if (!sb.toString().isEmpty()) {
            sb.append("Please try again")
            Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show()
        }


    }
}