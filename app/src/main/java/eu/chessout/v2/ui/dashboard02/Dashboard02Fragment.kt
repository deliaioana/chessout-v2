package eu.chessout.v2.ui.dashboard02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import eu.chessout.v2.R
import eu.chessout.v2.ui.club.ClubCreateDialogFragment
import kotlinx.android.synthetic.main.fragment_dashboard02.*

class Dashboard02Fragment : Fragment() {


    private lateinit var viewModel: Dashboard02ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard02, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model: Dashboard02ViewModel by viewModels()
        viewModel = model

        sign_out_card.setOnClickListener {
            var argsBundle = bundleOf("timeToLogOut" to true)
            view?.findNavController()?.navigate(R.id.signInActivity, argsBundle)
        }

        create_club_card.setOnClickListener {
            ClubCreateDialogFragment().show(childFragmentManager, "ClubCreateDialogFragment")
        }

        my_clubs_card.setOnClickListener { view ->
            view.findNavController()?.navigate(R.id.navigation_my_clubs_fragment)
        }

        tournaments_card.setOnClickListener { view ->

            val action = Dashboard02FragmentDirections
                .actionNavigationDashboard02ToTournamentsNavigation()
            view.findNavController()?.navigate(action)
        }

        players_card.setOnClickListener { view ->
            run {
                val action =
                    Dashboard02FragmentDirections.actionNavigationDashboard02ToClubPlayersFragment()
                view.findNavController()?.navigate(action)
            }
        }



        viewModel.myClubCreated.observe(viewLifecycleOwner, Observer { isMyClbCreated ->
            run {
                if (isMyClbCreated) {
                    // no need to create
                    create_club_card.visibility = View.GONE
                } else {
                    // user should be allowed to create
                    create_club_card.visibility = View.VISIBLE
                }
            }
        })

        viewModel.defaultClubExists.observe(viewLifecycleOwner, Observer { defaultClubExists ->
            run {
                if (defaultClubExists) {
                    tournaments_card.visibility = View.VISIBLE
                    players_card.visibility = View.VISIBLE
                } else {
                    tournaments_card.visibility = View.GONE
                    players_card.visibility = View.GONE
                }
            }
        })

        viewModel.initializeData()
    }

}
