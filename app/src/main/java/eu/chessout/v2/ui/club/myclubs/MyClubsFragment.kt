package eu.chessout.v2.ui.club.myclubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Club
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.my_clubs_fragment.*


class MyClubsFragment : Fragment() {
    private val logTag = eu.chessout.shared.Constants.LOG_TAG

    private lateinit var mApp: FirebaseApp
    private lateinit var mClubsReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: FirebaseUser

    private lateinit var mView: View
    private lateinit var mListView: ListView
    private lateinit var mAdapter: FirebaseListAdapter<Club>


    private lateinit var viewModel: MyClubsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_clubs_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyClubsViewModel::class.java)
        // TODO: Use the ViewModel

        mApp = FirebaseApp.getInstance()

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!

        //Firebase reference

        //Firebase reference
        val myClubsLocation: String = Constants.LOCATION_MY_CLUBS
            .replace(Constants.USER_KEY, mUser.uid)
        mClubsReference = FirebaseDatabase.getInstance().getReference(myClubsLocation)

        //create custom FirebaseListAdapter subclass

        //create custom FirebaseListAdapter subclass
        mAdapter = object : FirebaseListAdapter<Club>(
            activity,
            Club::class.java,
            R.layout.list_item_text,
            mClubsReference
        ) {
            override fun populateView(
                v: View,
                model: Club,
                position: Int
            ) {
                (v.findViewById<View>(R.id.list_item_text_simple_view) as TextView).text =
                    model.shortName
            }
        }
        list_view_my_clubs.adapter = mAdapter

    }

}
