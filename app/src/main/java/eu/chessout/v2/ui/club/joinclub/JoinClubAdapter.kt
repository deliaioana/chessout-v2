package eu.chessout.v2.ui.club.joinclub

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.Club
import eu.chessout.v2.R

class JoinClubAdapter(var clubList: ArrayList<Club>) {

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById<TextView>(R.id.list_item_text_simple_view)
    }
}
