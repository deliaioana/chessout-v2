package eu.chessout.v2.ui.dashboard02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
        val root = inflater.inflate(R.layout.fragment_dashboard02, container, false)



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(Dashboard02ViewModel::class.java)

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

        viewModel.initializeData()
    }

}
