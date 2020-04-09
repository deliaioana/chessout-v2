package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.app.AlertDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.FirebaseDatabase
import eu.chessout.shared.Constants
import eu.chessout.shared.dao.BasicApiResponse
import eu.chessout.shared.model.Game
import eu.chessout.shared.model.MyPayLoad
import eu.chessout.v2.model.BasicApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

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

            val myPayLoad = MyPayLoad()
            myPayLoad.authToken = "pleaseImplement"
            myPayLoad.event = MyPayLoad.Event.GAME_RESULT_UPDATED
            myPayLoad.gameType = "standard"
            myPayLoad.tournamentType = "standard"
            myPayLoad.tournamentId = tournamentId
            myPayLoad.roundId = roundId.toString()
            myPayLoad.tableId = tableNumber.toString()

            BasicApiService().gameResultUpdated(myPayLoad)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BasicApiResponse>() {
                    override fun onSuccess(value: BasicApiResponse?) {
                        Log.d(Constants.LOG_TAG, "Success${value?.message}")
                    }

                    override fun onError(e: Throwable?) {
                        Log.e(Constants.LOG_TAG, "Error ${e?.message}")
                    }
                })

            return null
        }
    }

}