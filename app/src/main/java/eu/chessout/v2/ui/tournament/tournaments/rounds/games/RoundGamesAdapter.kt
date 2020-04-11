package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Game
import eu.chessout.v2.R

class RoundGamesAdapter(val gameList: ArrayList<Game>) :
    RecyclerView.Adapter<RoundGamesAdapter.ItemHolder>() {

    private lateinit var clubId: String
    private lateinit var tournamentId: String
    private var roundId = -1
    private lateinit var fragmentManager: FragmentManager
    lateinit var context: Context
    var isAdmin = false

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_item_text_simple_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_text, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model: Game = gameList[position]
        val sb = StringBuffer()
        val whitePlayer = model.whitePlayer
        val blackPlayer = model.blackPlayer
        sb.append(model.actualNumber.toString() + ". ")
        sb.append(whitePlayer.name.toString() + " ")
        sb.append(formatResult(model.result) + " ")
        if (blackPlayer != null) {
            sb.append(blackPlayer.name.toString() + " ")
        }
        holder.textView.text = sb.toString()
        holder.textView.setOnClickListener {
            if (isAdmin && (null != model.blackPlayer)) {
                if (model.result == 0) {
                    RoundSetGameResultDialog(clubId, tournamentId, roundId, model).show(
                        this.fragmentManager,
                        "RoundSetGameResultDialog"
                    )
                } else {
                    Toast.makeText(
                        this.context,
                        "Use long click if you intend to update results",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Log.d(Constants.LOG_TAG, "You are not an admin")
            }
        }
        holder.textView.setOnLongClickListener {
            if (isAdmin && (null != model.blackPlayer)) {
                RoundSetGameResultDialog(clubId, tournamentId, roundId, model).show(
                    this.fragmentManager,
                    "RoundSetGameResultDialog"
                )
            }
            true
        }
    }

    private fun formatResult(result: Int): String? {
        var format: String? = null
        if (result == 1) {
            format = "1-0"
        } else if (result == 2) {
            format = "0-1"
        } else if (result == 0) {
            format = "---"
        } else if (result == 3) {
            format = "1/2-1/2"
        } else if (result == 4) {
            format = "1"
        }
        return format
    }

    fun updateList(newList: List<Game>) {
        gameList.clear()
        gameList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setIds(clubId: String, tournamentId: String, roundId: Int) {
        this.clubId = clubId
        this.tournamentId = tournamentId
        this.roundId = roundId
    }

    fun setFragmentManger(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

}