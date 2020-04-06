package eu.chessout.v2.ui.tournament.tournaments.rounds.games

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.Game
import eu.chessout.v2.R

class RoundGamesAdapter(val gameList: ArrayList<Game>) :
    RecyclerView.Adapter<RoundGamesAdapter.ItemHolder>() {


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
}