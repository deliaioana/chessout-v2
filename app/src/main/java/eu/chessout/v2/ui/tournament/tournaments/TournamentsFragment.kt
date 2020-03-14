package eu.chessout.v2.ui.tournament.tournaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.chessout.v2.R

class TournamentsFragment : Fragment() {

    companion object {
        fun newInstance() = TournamentsFragment()
    }

    private lateinit var viewModel: TournamentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tournaments_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val model: TournamentsViewModel by viewModels()
    }


}
