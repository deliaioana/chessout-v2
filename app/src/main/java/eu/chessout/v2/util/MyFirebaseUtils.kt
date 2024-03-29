package eu.chessout.v2.util

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import eu.chessdata.chesspairing.algoritms.fideswissduch.Algorithm
import eu.chessdata.chesspairing.algoritms.javafo.JavafoWrapp
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

    interface BoolListener {
        fun boolValueChanged(newValue: Boolean)
    }

    interface PlayersListener {
        fun listUpdated(players: List<Player>)
    }

    interface RankedPlayerListener {
        fun listUpdated(players: List<RankedPlayer>)
    }

    interface GamesListener {
        fun listUpdated(games: List<Game>)
    }

    interface LongListener {
        fun valueUpdated(value: Long)
    }

    interface PictureListener {
        fun valueUpdated(value: Picture)
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

    private fun buildGamesRef(tournamentKey: String, roundId: Int): DatabaseReference {
        val gamesLoc = Constants.LOCATION_ROUND_GAMES
            .replace(Constants.TOURNAMENT_KEY, tournamentKey)
            .replace(Constants.ROUND_NUMBER, roundId.toString())
        return FirebaseDatabase.getInstance().getReference(gamesLoc)
    }

    fun observeRoundHasGames(
        singleValueEvent: Boolean,
        tournamentKey: String,
        roundId: Int,
        boolListener: BoolListener
    ) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                boolListener.boolValueChanged(false)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    boolListener.boolValueChanged(true)
                }
            }
        }
        val gamesRef = buildGamesRef(tournamentKey, roundId)
        if (singleValueEvent) {
            gamesRef.addListenerForSingleValueEvent(valueEventListener)
        } else {
            gamesRef.addValueEventListener(valueEventListener)
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

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    fun generateGamesForRound(
        clubKey: String,
        tournamentKey: String,
        roundId: Int
    ) {
        if (!isCurrentUserAdmin(clubKey)) {
            return
        }


        var tournament: ChesspairingTournament =
            buildChessPairingTournament(clubKey, tournamentKey)
        val algorithm: Algorithm = JavafoWrapp()

        //<debug> collect tournament state
        val gson = Gson()
        val tournamentJson = gson.toJson(tournament)

        //<end debug>
        Log.d(Constants.LOG_TAG, "new_game = $tournamentJson")
        tournament = algorithm.generateNextRound(tournament)
        val rounds = tournament.rounds
        val round = rounds[rounds.size - 1]
        persistNewGames(clubKey, tournamentKey, round)
        Log.d(
            Constants.LOG_TAG,
            "persistNewGames has bean initiated"
        )
    }

    fun persistNewGames(
        clubKey: String,
        tournamentKey: String,
        round: ChesspairingRound
    ) {
        val firstTableNumber: Int = getFirstTableNumber(clubKey, tournamentKey)
        val tempList: List<Player> =
            getTournamentPlayers(tournamentKey)
        val playerMap: MutableMap<String, Player> =
            HashMap()
        for (player in tempList) {
            playerMap[player.playerKey] = player
        }

        //copy the games data
        val chesspairingGames = round.games
        val games: MutableList<Game> = java.util.ArrayList()
        for (chesspairingGame in chesspairingGames) {
            val game = Game()
            game.tableNumber = chesspairingGame.tableNumber
            game.actualNumber = chesspairingGame.tableNumber + firstTableNumber - 1
            game.whitePlayer = playerMap[chesspairingGame.whitePlayer.playerKey]
            if (chesspairingGame.blackPlayer != null) {
                //white player ad black player are present
                game.blackPlayer = playerMap[chesspairingGame.blackPlayer.playerKey]
            } else {
                game.result = 4
            }
            games.add(game)
        }
        val roundNumber = round.roundNumber.toString()
        val gamesLoc = Constants.LOCATION_ROUND_GAMES
            .replace(Constants.TOURNAMENT_KEY, tournamentKey!!)
            .replace(Constants.ROUND_NUMBER, roundNumber)
        val allGamesRef =
            FirebaseDatabase.getInstance().getReference(gamesLoc)
        for (gameItem in games) {
            val gameRef =
                allGamesRef.ref.child(java.lang.String.valueOf(gameItem.tableNumber))
            gameRef.setValue(gameItem)
        }
    }

    private fun getFirstTableNumber(
        clubKey: String,
        tournamentKey: String
    ): Int {
        val numbers = intArrayOf(1) //first number holds the result
        val latch = CountDownLatch(1)
        val tournamentLoc = Constants.LOCATION_TOURNAMENTS
            .replace(Constants.CLUB_KEY, clubKey)
            .replace(Constants.TOURNAMENT_KEY, tournamentKey)
        val tournamentRef =
            FirebaseDatabase.getInstance().getReference(tournamentLoc)
        tournamentRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    val tournament = dataSnapshot.getValue(Tournament::class.java)
                    numbers[0] = tournament!!.firstTableNumber
                }
                latch.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    Constants.LOG_TAG,
                    "getFirstTableNumber: " + databaseError.message
                )
                latch.countDown()
            }
        })
        try {
            latch.await()
        } catch (e: InterruptedException) {
            Log.e(Constants.LOG_TAG, "getFirstTableNumber " + e.message)
        }
        return numbers[0]
    }


    fun registerGamesListener(
        singleValueEvent: Boolean,
        tournamentId: String,
        roundId: Int,
        gamesListener: GamesListener
    ) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                gamesListener.listUpdated(ArrayList())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newList = ArrayList<Game>()
                for (item in p0.children) {
                    val game = item.getValue(Game::class.java)
                    newList.add(game!!)
                }
                gamesListener.listUpdated(newList)
            }

        }
        val gamesReference = buildGamesRef(tournamentId, roundId)
        if (singleValueEvent) {
            gamesReference.addListenerForSingleValueEvent(valueEventListener)
        } else {
            gamesReference.addValueEventListener(valueEventListener)
        }
    }


    fun registerCompletedRoundsListener(
        singleValueEvent: Boolean,
        tournamentId: String,
        totalRounds: Long,
        longListener: LongListener
    ) {
        val sectionNotRequired =
            "/" + Constants.ROUND_NUMBER + "/" + Constants.ROUND_ABSENT_PLAYERS
        val roundsLoc = Constants.LOCATION_ROUND_ABSENT_PLAYERS
            .replace(sectionNotRequired, "")
            .replace(Constants.TOURNAMENT_KEY, tournamentId)
        val roundsRef =
            FirebaseDatabase.getInstance().getReference(roundsLoc)

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // nothing to implement
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var roundsWithData = dataSnapshot.childrenCount
                if (roundsWithData == 0L) {
                    roundsWithData = 1
                }
                if (roundsWithData == totalRounds) {
                    longListener.valueUpdated(totalRounds)
                    return
                }


                if (!dataSnapshot.hasChild(roundsWithData.toString())) {
                    longListener.valueUpdated(roundsWithData)
                    return
                }

                // get the last round
                val vipRound = dataSnapshot.child(roundsWithData.toString())
                if (!vipRound.hasChild(Constants.GAMES)) {
                    return
                }
                val vipGames =
                    vipRound.child(Constants.GAMES)

                if (roundsWithData < totalRounds) {
                    var incrementRoundsWithData = true
                    /**
                     * iterate over games and if any results are 0 (not decided)
                     * then break the loop and set
                     * incrementRoundsWithData = false;
                     */
                    for (item in vipGames.children) {
                        val game = item.getValue(Game::class.java)
                        if (game!!.result === 0) {
                            incrementRoundsWithData = false
                            break
                        }
                    }
                    if (incrementRoundsWithData) {
                        longListener.valueUpdated(roundsWithData + 1)
                    }
                }
            }
        }
        if (singleValueEvent) {
            roundsRef.addListenerForSingleValueEvent(valueEventListener)
        } else {
            roundsRef.addValueEventListener(valueEventListener)
        }
    }

    private fun getProfilePictureRefString(clubId: String, playerId: String): String {
        return Constants.LOCATION_PLAYER_MEDIA_PROFILE_PICTURE
            .replace(Constants.CLUB_KEY, clubId)
            .replace(Constants.PLAYER_KEY, playerId)
    }

    private fun getProfilePictureRef(clubId: String, playerId: String): DatabaseReference {
        val refString = getProfilePictureRefString(clubId, playerId)
        return FirebaseDatabase.getInstance().getReference(refString)
    }

    fun setDefaultPicture(clubId: String, playerId: String, pictureName: String): Picture {
        val profilePictureRefString = getProfilePictureRefString(clubId, playerId)
        val pictureUri = "$profilePictureRefString/$pictureName";
        val picture = Picture("defaultPicture", pictureUri)

        val defaultPictureRef = getProfilePictureRef(clubId, playerId)
        defaultPictureRef.setValue(picture)
        return picture
    }


    fun registerDefaultPlayerPictureListener(
        singleValueEvent: Boolean,
        clubId: String,
        playerId: String,
        pictureListener: PictureListener
    ) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // nothing to do
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    val defaultPicture = dataSnapshot.getValue(Picture::class.java)
                    pictureListener.valueUpdated(defaultPicture!!)
                }
            }

        }
        val profilePictureRef = getProfilePictureRef(clubId, playerId)
        if (singleValueEvent) {
            profilePictureRef.addListenerForSingleValueEvent(valueEventListener)
        } else {
            profilePictureRef.addValueEventListener(valueEventListener)
        }

    }
}