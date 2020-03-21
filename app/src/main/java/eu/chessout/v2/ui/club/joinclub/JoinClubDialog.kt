package eu.chessout.v2.ui.club.joinclub

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

class JoinClubDialog() : DialogFragment() {

    private lateinit var mView: View
    private lateinit var joinClubModel: JoinClubModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val model: JoinClubModel by viewModels()
        joinClubModel = model

        joinClubModel.initializeModel()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select club to join")

        return builder.create()
    }

}