package eu.chessout.v2.ui.tournament.tournaments.rounds.pager

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import eu.chessout.shared.Constants
import eu.chessout.v2.R
import eu.chessout.v2.ui.tournament.tournaments.rounds.players.RoundAddAbsentPlayersDialog
import eu.chessout.v2.ui.tournament.tournaments.rounds.state.RoundStateFragment
import kotlinx.android.synthetic.main.rounds_pager_fragment.*

class RoundsPagerFragment : Fragment() {


    private val args: RoundsPagerFragmentArgs by navArgs()
    private lateinit var mView: View
    lateinit var viewModel: RoundPagerViewModel
    val stateFragments = HashMap<Int, RoundStateFragment>()
    lateinit var pagerAdapter: ScreenSlidePagerAdapter
    private val myObserver = Observer<Int> {
        pagerAdapter.updateCount(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.rounds_pager_fragment, container, false)
        setHasOptionsMenu(true)
        val model: RoundPagerViewModel by viewModels()
        viewModel = model
        viewModel.visibleRoundsCount.observe(viewLifecycleOwner, myObserver)
        viewModel.initializeModel(args.tournamentId, args.totalRounds)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pagerAdapter = ScreenSlidePagerAdapter(
            this.requireActivity(),
            viewModel.visibleRoundsCount.value!!.toInt()
        )
        roundsPager.adapter = pagerAdapter
        roundsPager.setPageTransformer(ZoomOutPageTransformer())
        roundsPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setPosition(position)
            }
        })
    }

    inner class ScreenSlidePagerAdapter(fa: FragmentActivity, private var countValue: Int) :
        FragmentStateAdapter(fa) {

        fun updateCount(newCount: Int) {
            this.countValue = newCount
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = countValue

        override fun createFragment(position: Int): Fragment {
            val roundId = position + 1;
            val stateFragment = RoundStateFragment(args.clubId, args.tournamentId, roundId)
            stateFragments[position] = stateFragment
            return stateFragment
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.round_absent_players_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addAbsentPlayers -> {
                val position = viewModel.position.value!!
                Log.d(Constants.LOG_TAG, "Present players for $position")
                val players = stateFragments[viewModel.position.value!!]!!
                    .getPresentPlayers()
                val roundId = viewModel.position.value!! + 1
                RoundAddAbsentPlayersDialog(
                    args.clubId,
                    args.tournamentId,
                    roundId,
                    players
                ).show(childFragmentManager, "RoundAddAbsentPlayersDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}