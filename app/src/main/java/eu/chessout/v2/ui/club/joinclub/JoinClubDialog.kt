package eu.chessout.v2.ui.club.joinclub

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import eu.chessout.shared.model.Club
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.dialog_join_club.*

class JoinClubDialog() : DialogFragment() {

    private lateinit var mView: View
    private lateinit var joinClubModel: JoinClubModel
    private val myListAdapter = JoinClubAdapter(arrayListOf())

    private val myObserver = Observer<List<Club>> { list ->
        list?.let {
            my_recycler_view.visibility = View.VISIBLE
            myListAdapter.updateArrayList(it)
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {


            val builder = AlertDialog.Builder(it)
            builder.setTitle("Select club to join")

            val inflater = requireActivity().layoutInflater

            mView = inflater.inflate(R.layout.dialog_join_club, null)
            builder.setView(mView)

            val model: JoinClubModel by viewModels()
            joinClubModel = model
            joinClubModel.liveClubs.observe(viewLifecycleOwner, myObserver)
            joinClubModel.initializeModel()

            my_recycler_view?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = myListAdapter
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}