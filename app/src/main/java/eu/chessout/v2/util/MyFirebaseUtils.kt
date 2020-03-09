package eu.chessout.v2.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import eu.chessout.shared.Constants
import eu.chessout.shared.model.DefaultClub

class MyFirebaseUtils {
    fun setDefaultClub(defaultClub: DefaultClub){
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
}