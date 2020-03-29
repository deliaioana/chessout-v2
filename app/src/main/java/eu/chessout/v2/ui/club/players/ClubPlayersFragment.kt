package eu.chessout.v2.ui.club.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.club_players_fragment.*

class ClubPlayersFragment : Fragment() {

    companion object {
        fun newInstance() = ClubPlayersFragment()
    }

    private lateinit var viewModel: ClubPlayersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView = inflater.inflate(R.layout.club_players_fragment, container, false)
        val model: ClubPlayersViewModel by viewModels()
        viewModel = model

        viewModel.isAdmin.observe(viewLifecycleOwner, Observer { isAdmin ->
            run {
                if (isAdmin) {
                    fab.visibility = View.VISIBLE
                } else {
                    fab.visibility = View.GONE
                }
            }
        })

        viewModel.initializeModel()



        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab.setOnClickListener { view ->
            val clubKey = viewModel.getClubKey().value!!
            ClubPlayerCreateDialogFragment().show(
                childFragmentManager,
                "ClubPlayerCreateDialogFragment"
            )
        }

    }
}
