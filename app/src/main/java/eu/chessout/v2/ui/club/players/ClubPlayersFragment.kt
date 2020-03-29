package eu.chessout.v2.ui.club.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.chessout.v2.R

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
        return mView
    }

}
