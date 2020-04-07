package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.app.AlertDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.FirebaseDatabase
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Game

class RoundSetGameResultDialog(
    val clubId: String,
    val tournamentId: String,
    val roundId: Int,
    val game: Game
) : DialogFragment() {

    val mWhitePlayer = "1 - 0 : ${game.whitePlayer.name} wins"
    val mBlackPlayer = "0 - 1 : ${game.blackPlayer.name} wins"
    val mNoPartner = hasNoPartner(game)
    val mNullGame = "1/2 = 1/2"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Set result for table ${game.tableNumber}")

        builder.setNegativeButton("Cancel") { _, _ -> dismiss() }
        val items = arrayOf(mWhitePlayer, mBlackPlayer, mNullGame)
        builder.setItems(items) { _, witch ->
            PersistResult(tournamentId, roundId, game.tableNumber).execute(witch)
        }

        return builder.create()
    }

    private fun hasNoPartner(game: Game): Boolean {
        return null == game.blackPlayer
    }

    class PersistResult(val tournamentId: String, val roundId: Int, private val tableNumber: Int) :
        AsyncTask<Int, Void, Void?>() {
        override fun doInBackground(vararg params: Int?): Void? {

            val result = params[0]!! + 1
            val resultLoc = Constants.LOCATION_GAME_RESULT
                .replace(Constants.TOURNAMENT_KEY, tournamentId)
                .replace(Constants.ROUND_NUMBER, roundId.toString())
                .replace(Constants.TABLE_NUMBER, tableNumber.toString())
            val resultRef =
                FirebaseDatabase.getInstance().getReference(resultLoc)
            resultRef.setValue(result)

            Log.d(Constants.LOG_TAG, "Update int = $result")
            return null
        }
    }

}