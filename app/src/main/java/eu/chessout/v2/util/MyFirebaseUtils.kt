package eu.chessout.v2.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import eu.chessout.shared.Constants
import eu.chessout.shared.model.*
import java.util.concurrent.CountDownLatch

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

    interface IsAdminListener {
        fun onIsAdmin(isAdmin: Boolean)
    }

    interface PlayersListener {
        fun listUpdated(players: List<Player>)
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
                if (null != defaultClub) {
                    listener.onDefaultClubValue(defaultClub!!)
                }
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
                if (null != defaultClub) {
                    listener.onDefaultClubValue(defaultClub!!)
                }
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

    fun isCurrentUserAdmin(clubKey: String?, isAdminListener: IsAdminListener) {
        val managers =
            arrayOf(false) //first boolean holds the result
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid
        val clubManagerLoc = Constants.LOCATION_CLUB_MANAGERS
            .replace(Constants.CLUB_KEY, clubKey!!)
            .replace(Constants.MANAGER_KEY, uid)
        val managerRef =
            FirebaseDatabase.getInstance().getReference(clubManagerLoc)
        managerRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    isAdminListener.onIsAdmin(true)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                isAdminListener.onIsAdmin(false)
            }
        })
    }

    fun awaitIsCurrentUserAdmin(clubKey: String?): Boolean {
        val managers =
            arrayOf(false) //first boolean holds the result
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid
        val clubManagerLoc = Constants.LOCATION_CLUB_MANAGERS
            .replace(Constants.CLUB_KEY, clubKey!!)
            .replace(Constants.MANAGER_KEY, uid)
        val managerRef =
            FirebaseDatabase.getInstance().getReference(clubManagerLoc)
        val isAdmin = arrayOf(false)
        val latch = CountDownLatch(1)
        val valueEventListener: ValueEventListener = object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    isAdmin[0] = true
                }
                latch.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                latch.countDown()
            }
        }
        managerRef.addListenerForSingleValueEvent(valueEventListener)
        latch.await()
        return isAdmin[0]
    }


    fun getClubPlayers(
        clubKey: String,
        singleValueEvent: Boolean,
        playersListener: PlayersListener
    ) {
        val clubPlayersLoc = Constants.LOCATION_CLUB_PLAYERS
            .replace(Constants.CLUB_KEY, clubKey)
        val clubPlayersRef =
            FirebaseDatabase.getInstance().getReference(clubPlayersLoc)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val players = ArrayList<Player>()
                for (item in dataSnapshot.children) {
                    val player = item.getValue(Player::class.java)
                    players.add(player!!)
                }
                playersListener.listUpdated(players)
            }

            override fun onCancelled(p0: DatabaseError) {
                // nothing to do
            }
        }
        if (singleValueEvent) {
            clubPlayersRef.addListenerForSingleValueEvent(valueEventListener)
        } else {
            clubPlayersRef.addValueEventListener(valueEventListener)
        }
    }

    fun getTournamentPlayers(
        tournamentId: String,
        singleValueEvent: Boolean,
        playersListener: PlayersListener
    ) {
        val playersLoc = Constants.LOCATION_TOURNAMENT_PLAYERS
            .replace(Constants.TOURNAMENT_KEY, tournamentId)
        val mReference =
            FirebaseDatabase.getInstance().getReference(playersLoc)

        val valueEventListener: ValueEventListener =
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tournamentPlayers = ArrayList<Player>()
                    for (item in dataSnapshot.children) {
                        val rankedPlayer = item.getValue(Player::class.java)
                        tournamentPlayers.add(rankedPlayer!!)
                    }
                    playersListener.listUpdated(tournamentPlayers)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
        if (singleValueEvent) {
            mReference.addListenerForSingleValueEvent(valueEventListener)
        } else {
            mReference.addValueEventListener(valueEventListener)
        }
    }

    fun getMissingPlayers(
        tournamentId: String,
        roundId: Int,
        singleValueEvent: Boolean,
        playersListener: PlayersListener
    ) {
        val roundPlayersLoc = Constants.LOCATION_ROUND_ABSENT_PLAYERS
            .replace(Constants.TOURNAMENT_KEY, tournamentId)
            .replace(Constants.ROUND_NUMBER, roundId.toString())
        val mReference = FirebaseDatabase.getInstance()
            .getReference(roundPlayersLoc)

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val absentList = ArrayList<Player>()
                for (item in dataSnapshot.children) {
                    val player = item.getValue(Player::class.java)
                    absentList.add(player!!)
                }
                playersListener.listUpdated(absentList)
            }

            override fun onCancelled(p0: DatabaseError) {
                // nothing to implement
            }
        }
        if (singleValueEvent) {
            mReference.addListenerForSingleValueEvent(valueEventListener)
        } else {
            mReference.addValueEventListener(valueEventListener)
        }
    }

    fun awaitGetTournamentPlayers(tournamentId: String): List<Player> {
        val playersLoc = Constants.LOCATION_TOURNAMENT_PLAYERS
            .replace(Constants.TOURNAMENT_KEY, tournamentId)
        val mReference =
            FirebaseDatabase.getInstance().getReference(playersLoc)
        val players = ArrayList<Player>()
        val latch = CountDownLatch(1)
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.children) {
                    val player = item.getValue(Player::class.java)
                    players.add(player!!)
                }
                latch.countDown()
            }

            override fun onCancelled(p0: DatabaseError) {
                latch.countDown()
            }
        }
        mReference.addListenerForSingleValueEvent(valueEventListener)
        latch.await()

        return players
    }

}