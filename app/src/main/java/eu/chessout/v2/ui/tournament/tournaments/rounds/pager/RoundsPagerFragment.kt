package eu.chessout.v2.ui.tournament.tournaments.rounds.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.chessout.v2.R
import eu.chessout.v2.ui.tournament.tournaments.rounds.state.RoundStateFragment
import kotlinx.android.synthetic.main.rounds_pager_fragment.*

class RoundsPagerFragment : Fragment() {

    private val args: RoundsPagerFragmentArgs by navArgs()
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.rounds_pager_fragment, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pagerAdapter = ScreenSlidePagerAdapter(this.requireActivity())
        roundsPager.adapter = pagerAdapter
        roundsPager.setPageTransformer(ZoomOutPageTransformer())

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = args.totalRounds

        override fun createFragment(position: Int): Fragment =
            RoundStateFragment(args.clubId, args.tournamentId, position)
    }
}