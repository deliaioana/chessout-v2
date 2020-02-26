package eu.chessout.v2.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.v2.R

class DashboardAdapter(
    var context: Context,
    var arrayList: ArrayList<DashboardModel>
) : RecyclerView.Adapter<DashboardAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_layout_list_item, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        var dashboardModel: DashboardModel = arrayList.get(position)

        holder.imageView.setImageResource(dashboardModel.iconsChar!!)
        holder.textView.text = dashboardModel.iconString

        holder.textView.setOnClickListener {
            Toast.makeText(context, dashboardModel.iconString, Toast.LENGTH_LONG).show()
        }
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.icon_image)
        var textView = itemView.findViewById<TextView>(R.id.icon_text)
    }
}