package eu.chessout.v2.util

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Club
import eu.chessout.shared.model.DefaultClub
import eu.chessout.shared.model.Tournament
import eu.chessout.shared.model.User

class MyFirebaseUtils {
    fun setDefaultClub(defaultClub: DefaultClub) {
        val database =
            FirebaseDatabase.getInstance()
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid

        val defaultClubLocation: String = Constants.LOCATION_DEFAULT_CLUB
            .replace(Constants.USER_KEY, uid)
        val managedClubRef =
            database.getReference(defaultClubLocation)
        managedClubRef.setValue(defaultClub)
    }

    interface DefaultClubListener {
        fun onDefaultClubValue(defaultClub: DefaultClub)
    }

    fun getDefaultClubSingleValueListener(
        listener: DefaultClubListener
    ) {
        val database =
            FirebaseDatabase.getInstance()
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid
        val defaultClubLocation = Constants.LOCATION_DEFAULT_CLUB
            .replace(Constants.USER_KEY, uid)
        val defaultClubRef =
            database.getReference(defaultClubLocation)
        defaultClubRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val defaultClub = dataSnapshot.getValue(DefaultClub::class.java)
                Log.d(Constants.LOG_TAG, "default Club found")
                listener.onDefaultClubValue(defaultClub!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getDefaultClubListener(
        listener: DefaultClubListener
    ) {
        val database =
            FirebaseDatabase.getInstance()
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid
        val defaultClubLocation = Constants.LOCATION_DEFAULT_CLUB
            .replace(Constants.USER_KEY, uid)
        val defaultClubRef =
            database.getReference(defaultClubLocation)
        defaultClubRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val defaultClub = dataSnapshot.getValue(DefaultClub::class.java)
                Log.d(Constants.LOG_TAG, "default Club found")
                listener.onDefaultClubValue(defaultClub!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setMyClub(myClub: DefaultClub) {
        val database =
            FirebaseDatabase.getInstance()
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid

        val defaultClubLocation: String = Constants.LOCATION_L_MY_CLUB
            .replace(Constants.USER_KEY, uid)
        val managedClubRef =
            database.getReference(defaultClubLocation)
        managedClubRef.setValue(myClub)
    }

    fun setManager(
        displayName: String,
        email: String,
        clubId: String,
        uid: String
    ) {
        //set manager
        val database = FirebaseDatabase.getInstance()

        val managersLocation: String = Constants.LOCATION_CLUB_MANAGERS
            .replace(Constants.CLUB_KEY, clubId!!)
            .replace(Constants.MANAGER_KEY, uid)
        val clubManager = User(displayName, email)
        val managersRef =
            database.getReference(managersLocation)
        managersRef.setValue(clubManager)
    }

    fun addToMyClubs(uid: String, clubId: String, club: Club) {
        val database = FirebaseDatabase.getInstance()
        val myClubLocation: String = Constants.LOCATION_MY_CLUBS
            .replace(Constants.USER_KEY, uid)
            .replace(Constants.CLUB_KEY, clubId)
        val myClubsRef =
            database.getReference(myClubLocation)
        myClubsRef.setValue(club)
    }

    fun updateTournamentReversedOrder(
        clubKey: String?,
        tournamentKey: String
    ) {
        val database =
            FirebaseDatabase.getInstance()
        val tournamentLocation = Constants.LOCATION_TOURNAMENTS
            .replace(Constants.CLUB_KEY, clubKey!!) + "/" + tournamentKey
        val tournamentRef =
            database.getReference(tournamentLocation)
        tournamentRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tournament: Tournament? = dataSnapshot.getValue(Tournament::class.java)
                if (tournament != null) {
                    val timeStamp: Long = tournament.dateCreatedGetLong()
                    val reversedDateCreated = 0 - timeStamp
                    tournamentRef.child("reversedDateCreated").setValue(reversedDateCreated)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}