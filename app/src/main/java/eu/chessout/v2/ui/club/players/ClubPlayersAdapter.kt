package eu.chessout.v2.ui.club.players

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.Player
import eu.chessout.v2.R
import eu.chessout.v2.ui.club.FollowPlayerDialog

class ClubPlayersAdapter(var playerList: ArrayList<Player>) :
    RecyclerView.Adapter<ClubPlayersAdapter.ItemHolder>() {

    private lateinit var fragmentManager: FragmentManager

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
        val player: Player = playerList[position]
        holder.textView.text = player.name
        holder.textView.setOnClickListener {
            FollowPlayerDialog(player).show(fragmentManager, "FollowPlayerDialog")
        }
    }

    fun updateList(newList: List<Player>) {
        playerList.clear()
        playerList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }
}