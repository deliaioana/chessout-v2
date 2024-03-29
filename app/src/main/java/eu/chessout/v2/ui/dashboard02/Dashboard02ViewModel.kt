package eu.chessout.v2.ui.dashboard02

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import eu.chessout.shared.Constants
import eu.chessout.shared.model.DefaultClub
import eu.chessout.v2.util.MyFirebaseUtils
import kotlin.concurrent.thread


class Dashboard02ViewModel : ViewModel() {

    companion object{
        private const val LOG_TAG = Constants.LOG_TAG
    }

    private var dataInitialized = false
    private val _text = MutableLiveData<String>().apply {
        value = "This is detail 2 fragment"
    }

    private val _showItem = MutableLiveData<Boolean>().apply {
        value = true
    }

    private val _countValue = MutableLiveData<Int>().apply {
        value = 0;
    }

    private val _myClubCreated = MutableLiveData<Boolean>().apply {
        value = true
    }

    val text: LiveData<String> = _text
    val countValue: LiveData<Int> = _countValue
    val myClubCreated: LiveData<Boolean> = _myClubCreated
    val defaultClubExists = MutableLiveData<Boolean>(false)


    fun initializeCount() {
        thread() {
            while (countValue.value!! < 100) {
                _countValue.postValue(countValue.value!! + 1)
                Thread.sleep(1000)
            }
        }
    }

    fun initializeData() {
        if (!dataInitialized) {
            dataInitialized = true;
            checkClubCreated()
        }

        class DefaultClubListener : MyFirebaseUtils.DefaultClubListener {
            override fun onDefaultClubValue(defaultClub: DefaultClub) {
                defaultClubExists.value = true
            }
        }
        MyFirebaseUtils().getDefaultClubSingleValueListener(DefaultClubListener())
    }

    private fun checkClubCreated() {

        // database references
        val database =
            FirebaseDatabase.getInstance()
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid

        val myClubLocation: String = Constants.LOCATION_L_MY_CLUB
            .replace(Constants.USER_KEY, uid)
        val myClubRef =
            database.getReference(myClubLocation)

        // listener
        val clubListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _myClubCreated.value = dataSnapshot.exists()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        myClubRef.addValueEventListener(clubListener)
    }
}
