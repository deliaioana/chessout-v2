package eu.chessout.v2.util

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import eu.chessdata.chesspairing.model.*
import eu.chessout.shared.Constants
import eu.chessout.shared.model.*
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList
import kotlin.collections.set

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

    interface RankedPlayerListener {
        fun listUpdated(players: List<RankedPlayer>)
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

    fun isCurrentUserAdmin(clubKey: String): Boolean {
        val managers =
            arrayOf(false) //first boolean holds the result
        val uid =
            FirebaseAuth.getInstance().currentUser!!.uid
        val clubManagerLoc = Constants.LOCATION_CLUB_MANAGERS
            .replace(Constants.CLUB_KEY, clubKey!!)
            .replace(Constants.MANAGER_KEY, uid)
        val managerRef =
            FirebaseDatabase.getInstance().getReference(clubManagerLoc)
        val latch = CountDownLatch(1)
        val adminItem = arrayListOf(false)
        managerRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    adminItem[0] = true
                }
                latch.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                latch.countDown()
            }
        })
        latch.await()
        return adminItem[0]
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


    fun refreshTournamentInitialOrder(clubId: String, tournamentId: String) {
        if (!isCurrentUserAdmin(clubId)) {
            return
        }

        val tournament: ChesspairingTournament = buildChessPairingTournament(
            clubId, tournamentId
        )

        val clubKey = clubId
        val tournamentKey = tournamentId

        //initial order


        //initial order
        val rankedPlayers: List<RankedPlayer> =
            getTournamentInitialOrder(tournamentKey)
        val orderedMap: MutableMap<String, RankedPlayer> =
            LinkedHashMap()

        var rank = 1
        if (rankedPlayers.size > 0) {
            for (player in rankedPlayers) {
                player.tournamentInitialOrder = rank++
                orderedMap[player.playerKey] = player
            }
        }


        for (player in tournament.players) {
            val playerKey = player.playerKey
            if (!orderedMap.containsKey(playerKey)) {
                val rankedPlayer = RankedPlayer(player, tournamentKey, clubKey)
                rankedPlayer.tournamentInitialOrder = rank++
                orderedMap[rankedPlayer.playerKey] = rankedPlayer
            }
        }

        //delete initial order

        //delete initial order
        val initialOrder = Constants.LOCATION_TOURNAMENT_PLAYER_INITIAL_ORDER
            .replace(Constants.TOURNAMENT_KEY, tournamentKey)
            .replace("/" + Constants.PLAYER_KEY, "")
        val initialOrderReference =
            FirebaseDatabase.getInstance()
                .getReference(initialOrder)
        initialOrderReference.removeValue()

        //set new order
        for (rankedPlayer in orderedMap.values) {
            val tournamentOrderLocation =
                Constants.LOCATION_TOURNAMENT_PLAYER_INITIAL_ORDER
                    .replace(Constants.TOURNAMENT_KEY, tournamentKey)
                    .replace(Constants.PLAYER_KEY, rankedPlayer.playerKey)
            val updatedOrderReference =
                FirebaseDatabase.getInstance()
                    .getReference(tournamentOrderLocation)
            updatedOrderReference.setValue(rankedPlayer)
        }

        Log.d(
            Constants.LOG_TAG,
            "End of eu.chessdata.utils.MyFirebaseUtils.refreshTournamentInitialOrder"
        )

        Log.d(Constants.LOG_TAG, "Refresh initial order clubId=$clubId tournamentId=$tournamentId")
    }

    /**
     * it collects the entire required data from firebase in order to build the current state
     * of the tournament
     *
     * @param clubKey
     * @param tournamentKey
     * @return
     */
    fun buildChessPairingTournament(
        clubKey: String,
        tournamentKey: String
    ): ChesspairingTournament {
        val chesspairingTournament = ChesspairingTournament()
        val latch1 = CountDownLatch(1)
        //get the general description
        val tournamentLoc = Constants.LOCATION_TOURNAMENT
            .replace(Constants.CLUB_KEY, clubKey!!)
            .replace(Constants.TOURNAMENT_KEY, tournamentKey!!)
        val tournamentRef =
            FirebaseDatabase.getInstance().getReference(tournamentLoc)
        tournamentRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tournament = dataSnapshot.getValue(Tournament::class.java)
                if (tournament != null) {
                    chesspairingTournament.name = tournament.name
                    chesspairingTournament.description = tournament.description
                    chesspairingTournament.totalRounds = tournament.totalRounds
                    chesspairingTournament.totalRounds = tournament.totalRounds
                }
                tournamentRef.removeEventListener(this)
                latch1.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(Constants.LOG_TAG, databaseError.message)
                latch1.countDown()
            }
        })
        try {
            latch1.await()
        } catch (e: InterruptedException) {
            Log.e(Constants.LOG_TAG, "tournamentDetailsError: " + e.message)
        }

        //populate players
        val players: List<Player> =
            getTournamentPlayers(tournamentKey)

        //initial order
        val rankedPlayers: List<RankedPlayer> =
            getTournamentInitialOrder(tournamentKey)
        val initialOrder: MutableMap<String, Int> =
            HashMap()
        var weHaveInitialOrder = false
        if (rankedPlayers.size > 0) {
            weHaveInitialOrder = true
            var i = 1
            for (player in rankedPlayers) {
                initialOrder[player.getPlayerKey()] = i++
            }
        }
        val chesspairingPlayers: MutableList<ChesspairingPlayer> =
            java.util.ArrayList()
        var i = 0
        var k = -1
        for (player in players) {
            i++ //set the player order as the natural one collected from firebase
            val chesspairingPlayer: ChesspairingPlayer = scanPlayer(player)
            if (weHaveInitialOrder) {
                if (initialOrder.containsKey(chesspairingPlayer.playerKey)) {
                    chesspairingPlayer.initialOrderId = initialOrder[chesspairingPlayer.playerKey]!!
                } else {
                    k++
                    val order = k + initialOrder.size
                    chesspairingPlayer.initialOrderId = k + initialOrder.size
                }
            } else {
                chesspairingPlayer.initialOrderId = i
            }
            chesspairingPlayers.add(chesspairingPlayer)
        }
        val comparator =
            Comparator { p1: ChesspairingPlayer, p2: ChesspairingPlayer -> p1.initialOrderId - p2.initialOrderId }
        chesspairingPlayers.sortWith(comparator)
        chesspairingTournament.players = chesspairingPlayers

        //populate the rounds
        chesspairingTournament.rounds = getTournamentRounds(tournamentKey)
        return chesspairingTournament
    }

    fun getTournamentInitialOrder(tournamentKey: String): List<RankedPlayer> {
        val playerList: ArrayList<RankedPlayer> =
            java.util.ArrayList<RankedPlayer>()
        val latch = CountDownLatch(1)
        val playersLoc = Constants.LOCATION_TOURNAMENT_INITIAL_ORDER
            .replace(Constants.TOURNAMENT_KEY, tournamentKey!!)
        val playersRef =
            FirebaseDatabase.getInstance().getReference(playersLoc)
        playersRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val it: Iterator<DataSnapshot> =
                    dataSnapshot.children.iterator()
                while (it.hasNext()) {
                    val player: RankedPlayer? = it.next().getValue(RankedPlayer::class.java)
                    playerList.add(player!!)
                }
                latch.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(Constants.LOG_TAG, databaseError.message)
                latch.countDown()
            }
        })
        try {
            latch.await()
        } catch (e: InterruptedException) {
            // nothing to do
        }
        val comparator: Comparator<RankedPlayer> =
            Comparator<RankedPlayer> { pa, pb -> pa.getTournamentInitialOrder() - pb.getTournamentInitialOrder() }
        playerList.sortWith(comparator)
        return playerList
    }


    fun observeTournamentInitialOrder(
        singleValueEvent: Boolean,
        tournamentKey: String,
        rankedPlayerListener: RankedPlayerListener
    ) {
        val playerList: ArrayList<RankedPlayer> =
            java.util.ArrayList<RankedPlayer>()
        val playersLoc = Constants.LOCATION_TOURNAMENT_INITIAL_ORDER
            .replace(Constants.TOURNAMENT_KEY, tournamentKey!!)
        val playersRef =
            FirebaseDatabase.getInstance().getReference(playersLoc)

        val valueEventListener = object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                playerList.clear()
                val it: Iterator<DataSnapshot> =
                    dataSnapshot.children.iterator()
                while (it.hasNext()) {
                    val player: RankedPlayer? = it.next().getValue(RankedPlayer::class.java)
                    playerList.add(player!!)
                }
                val comparator: Comparator<RankedPlayer> =
                    Comparator<RankedPlayer> { pa, pb -> pa.getTournamentInitialOrder() - pb.getTournamentInitialOrder() }
                playerList.sortWith(comparator)
                rankedPlayerListener.listUpdated(playerList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(Constants.LOG_TAG, databaseError.message)
            }
        }
        if (singleValueEvent) {
            playersRef.addListenerForSingleValueEvent(valueEventListener)
        } else {
            playersRef.addValueEventListener(valueEventListener)
        }
    }

    fun getTournamentPlayers(tournamentKey: String?): List<Player> {
        val playerList: MutableList<Player> = java.util.ArrayList()
        val latch = CountDownLatch(1)
        val playersLoc = Constants.LOCATION_TOURNAMENT_PLAYERS
            .replace(Constants.TOURNAMENT_KEY, tournamentKey!!)
        val playersRef =
            FirebaseDatabase.getInstance().getReference(playersLoc)
        playersRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val it: Iterator<DataSnapshot> =
                    dataSnapshot.children.iterator()
                while (it.hasNext()) {
                    val player = it.next().getValue(Player::class.java)
                    playerList.add(player!!)
                }
                latch.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(Constants.LOG_TAG, databaseError.message)
                latch.countDown()
            }
        })
        try {
            latch.await()
        } catch (e: InterruptedException) {
            Log.e(Constants.LOG_TAG, "getTournamentPlayers: " + e.message)
        }
        return playerList
    }


    fun getTournamentRounds(tournamentKey: String?): List<ChesspairingRound>? {
        val chesspairingRounds: MutableList<ChesspairingRound> =
            java.util.ArrayList()
        val latch = CountDownLatch(1)
        val sectionNotRequired =
            "/" + Constants.ROUND_NUMBER + "/" + Constants.ROUND_ABSENT_PLAYERS
        //get the rounds data
        val roundsLoc = Constants.LOCATION_ROUND_ABSENT_PLAYERS
            .replace(sectionNotRequired, "")
            .replace(Constants.TOURNAMENT_KEY, tournamentKey!!)
        val roundsRef =
            FirebaseDatabase.getInstance().getReference(roundsLoc)
        roundsRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val roundIterator: Iterator<DataSnapshot> =
                    dataSnapshot.children.iterator()
                while (roundIterator.hasNext()) {
                    val snapshot =
                        roundIterator.next()
                    val chesspairingRound = ChesspairingRound()
                    //get the round number
                    val roundNumber = snapshot.key
                    chesspairingRound.roundNumber = Integer.valueOf(roundNumber!!)
                    val absentPlayers: MutableList<Player?> =
                        java.util.ArrayList()
                    val games: List<Game> = java.util.ArrayList<Game>()

                    //get the absentPlayers
                    if (snapshot.hasChild(Constants.ROUND_ABSENT_PLAYERS)) {
                        val playersSnapshot =
                            snapshot.child(Constants.ROUND_ABSENT_PLAYERS)
                        val playersIterator: Iterator<DataSnapshot> =
                            playersSnapshot.children.iterator()
                        while (playersIterator.hasNext()) {
                            val player = playersIterator.next().getValue(Player::class.java)
                            absentPlayers.add(player)
                        }
                    }
                    //List<ChesspairingPlayer> absentChesspairingPlayers = new ArrayList<ChesspairingPlayer>();
                    for (player in absentPlayers) {
                        val chesspairingPlayer: ChesspairingPlayer =
                            scanPlayer(player!!)
                        chesspairingPlayer.isPresent = false
                        chesspairingRound.addAbsentPlayer(chesspairingPlayer)
                    }
                    Log.i(Constants.LOG_TAG, "Time to decode game")
                    //get the games
                    if (snapshot.hasChild(Constants.GAMES)) {
                        val gamesSnapshot =
                            snapshot.child(Constants.GAMES)
                        val gamesIterator: Iterator<DataSnapshot> =
                            gamesSnapshot.children.iterator()
                        val chesspairingGames: MutableList<ChesspairingGame> =
                            java.util.ArrayList()
                        val presentPlayers: MutableList<ChesspairingPlayer> =
                            java.util.ArrayList()
                        while (gamesIterator.hasNext()) {
                            val game: Game? = gamesIterator.next().getValue(Game::class.java)
                            val chesspairingGame: ChesspairingGame =
                                scanGame(game!!)
                            chesspairingGames.add(chesspairingGame)
                            presentPlayers.add(chesspairingGame.whitePlayer)
                            if (chesspairingGame.blackPlayer != null) {
                                presentPlayers.add(chesspairingGame.blackPlayer)
                            }
                        }
                        chesspairingRound.games = chesspairingGames
                        chesspairingRound.presentPlayers = presentPlayers
                    }

                    //add the round
                    chesspairingRounds.add(chesspairingRound)
                }
                latch.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                latch.countDown()
            }
        })
        //wait for the thread to finish computation
        try {
            latch.await()
        } catch (e: InterruptedException) {
            Log.e(Constants.LOG_TAG, "tournamentDetailsError: " + e.message)
        }
        return chesspairingRounds
    }

    /**
     * Converts Player onto ChesspairingPlayer
     * @param player
     * @return
     */
    fun scanPlayer(player: Player): ChesspairingPlayer {
        val chesspairingPlayer = ChesspairingPlayer()
        chesspairingPlayer.name = player.name
        val elo: Int
        elo = if (player.elo !== 0) {
            player.elo
        } else {
            player.clubElo
        }
        chesspairingPlayer.elo = elo
        chesspairingPlayer.playerKey = player.playerKey
        return chesspairingPlayer
    }

    fun scanGame(game: Game): ChesspairingGame {
        val chesspairingGame = ChesspairingGame()
        chesspairingGame.tableNumber = game.getActualNumber()
        chesspairingGame.whitePlayer =
            scanPlayer(game.getWhitePlayer())
        if (game.getBlackPlayer() != null) {
            chesspairingGame.blackPlayer =
                scanPlayer(game.getBlackPlayer())
        }
        val result: ChesspairingResult =
            convertResult(game.getResult())
        chesspairingGame.result = result
        return chesspairingGame
    }

    fun convertResult(result: Int): ChesspairingResult {
        when (result) {
            0 -> return ChesspairingResult.NOT_DECIDED
            1 -> return ChesspairingResult.WHITE_WINS
            2 -> return ChesspairingResult.BLACK_WINS
            3 -> return ChesspairingResult.DRAW_GAME
            4 -> return ChesspairingResult.BYE
        }
        throw IllegalStateException("New result type. please convert: $result")
    }

}