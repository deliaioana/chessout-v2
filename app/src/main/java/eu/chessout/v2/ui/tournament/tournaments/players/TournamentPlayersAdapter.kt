package eu.chessout.v2.ui.tournament.tournaments.players

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.RankedPlayer
import eu.chessout.v2.R

class TournamentPlayersAdapter(var playerList: ArrayList<RankedPlayer>) :
    RecyclerView.Adapter<TournamentPlayersAdapter.ItemHolder>() {


    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById<TextView>(R.id.list_item_text_simple_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_text, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val player: RankedPlayer = playerList[position]
        holder.textView.text = player.playerName
    }

    fun updateList(newList: List<RankedPlayer>) {
        playerList.clear()
        playerList.addAll(newList)
        notifyDataSetChanged()
    }
}