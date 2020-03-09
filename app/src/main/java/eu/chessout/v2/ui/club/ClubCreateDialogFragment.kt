package eu.chessout.v2.ui.club

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Club
import eu.chessout.shared.model.DefaultClub
import eu.chessout.shared.model.User
import eu.chessout.v2.R
import eu.chessout.v2.util.MyFirebaseUtils

class ClubCreateDialogFragment : DialogFragment() {

    private val LOG_TAG: String = Constants.LOG_TAG
    private val myFirebaseUtils = MyFirebaseUtils()
    private lateinit var mView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(this!!.activity!!)
        val inflater = this!!.activity!!.layoutInflater

        mView = inflater.inflate(R.layout.club_create_dialog, null)
        builder.setView(mView)

        builder.setNegativeButton("Cancel") { _, _ ->
            dismiss()
        }

        builder.setPositiveButton("Create club") { _, _ ->
            persistClub()
        }
        return builder.create()
    }


    private fun buildClub(): Club {
        val name =
            (mView.findViewById<View>(R.id.clubName) as EditText).text.toString()
        val shortName =
            (mView.findViewById<View>(R.id.shortName) as EditText).text.toString()
        val email =
            (mView.findViewById<View>(R.id.email) as EditText).text.toString()
        val country =
            (mView.findViewById<View>(R.id.country) as EditText).text.toString()
        val city =
            (mView.findViewById<View>(R.id.city) as EditText).text.toString()
        val homePage =
            (mView.findViewById<View>(R.id.homePage) as EditText).text.toString()
        val description =
            (mView.findViewById<View>(R.id.clubDescription) as EditText).text
                .toString()
        return Club(name, shortName, email, country, city, homePage, description)
    }

    private fun persistClub() {
        val club: Club = buildClub()
        val database =
            FirebaseDatabase.getInstance()
        val auth =
            FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser
        val uid = firebaseUser!!.uid
        val displayName = firebaseUser.displayName

        //create the club
        val clubs =
            database.getReference(Constants.CLUBS)
        val clubRef = clubs.push()
        clubRef.setValue(club)
        val clubId = clubRef.key
        Log.d(LOG_TAG, "clubId = $clubId")

        //set manager
        val managersLocation: String = Constants.LOCATION_CLUB_MANAGERS
            .replace(Constants.CLUB_KEY, clubId!!)
            .replace(Constants.MANAGER_KEY, uid)
        val clubManager = User(firebaseUser.displayName, firebaseUser.email)
        val managersRef =
            database.getReference(managersLocation)
        managersRef.setValue(clubManager)

        //add to my_clubs club
        val myClubLocation: String = Constants.LOCATION_MY_CLUB
            .replace(Constants.USER_KEY, uid)
            .replace(Constants.CLUB_KEY, clubId)
        val myClubsRef =
            database.getReference(myClubLocation)
        myClubsRef.setValue(club)

        //settings set defaultClub key;
        val defaultClub = DefaultClub(clubId, club.shortName)
        myFirebaseUtils.setDefaultClub(defaultClub)
    }
}