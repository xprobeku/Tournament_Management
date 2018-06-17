package mn.xpro.tournamentmanagement;

/**
 * Created by xpro on 4/7/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TournamentManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // Player table name
    private static final String TABLE_PLAYER = "player";

    // Tournament table name
    private static final String TABLE_TOURNAMENT = "tournament";

    // King of the hill table name
    private static final String TABLE_KING_OF_THE_HILL = "king_of_the_hill";

    // Match record table
    private static final String TABLE_MATCH = "matches";

    // Playoff table name
    private static final String TABLE_PLAYOFF = "playoff";

    // Standing table name
    private static final String TABLE_STANDING = "standing";

    // User Table Columns name
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Player Table Columns name
    private static final String COLUMN_PLAYER_ID = "player_id";
    private static final String COLUMN_PLAYER_NAME = "player_name";
    private static final String COLUMN_PLAYER_TYPE = "player_type";
    private static final String COLUMN_PLAYER_DATE = "player_date";
    private static final String COLUMN_PLAYER_DESCRIPTION = "player_description";
    private static final String COLUMN_PLAYER_TOURNAMENT_ID = "player_tournament_id";


    // Tournament Table Columns name
    private static final String COLUMN_TOURNAMENT_ID = "tournament_id";
    private static final String COLUMN_TOURNAMENT_NAME = "tournament_name";
    private static final String COLUMN_TOURNAMENT_DATE = "tournament_date";
    private static final String COLUMN_TOURNAMENT_TYPE = "tournament_type";
    private static final String COLUMN_TOURNAMENT_GAME_TYPE = "tournament_game_type";
    private static final String COLUMN_TOURNAMENT_STATUS = "tournament_status";
    private static final String COLUMN_TOURNAMENT_WINNER_ID = "tournament_winner";


    // Match table Columns name
    private static final String COLUMN_MATCH_ID = "match_id";
    private static final String COLUMN_MATCH_NUMBER = "match_number";
    private static final String COLUMN_MATCH_TOURNAMENT_ID = "match_tournament_id";
    private static final String COLUMN_MATCH_PLAYER_1 = "match_player_1";
    private static final String COLUMN_MATCH_PLAYER_2 = "match_player_2";
    private static final String COLUMN_MATCH_POINT_1 = "match_player_1_point";
    private static final String COLUMN_MATCH_POINT_2 = "match_player_2_point";
    private static final String COLUMN_MATCH_DATE = "match_date";
    private static final String COLUMN_MATCH_WINNER_ID = "match_winner_id";
    private static final String COLUMN_MATCH_STATUS = "match_status";
    private static final String COLUMN_MATCH_ROUND = "match_round";

    // King of the hill columns name
    private static final String COLUMN_KOTH_ID = "KOTH_ID";
    private static final String COLUMN_KOTH_TOURNAMENT_ID = "KOTH_TOURNAMENT_ID";
    private static final String COLUMN_KOTH_LIST = "KOTH_LIST";
    private static final String COLUMN_KOTH_ROUND = "KOTH_ROUND";
    private static final String COLUMN_KOTH_NUMBER = "KOTH_NUMBER";

    // Playoff columns name
    private static final String COLUMN_PLAYOFF_ID = "id";
    private static final String COLUMN_PLAYOFF_TOURNAMENT_ID = "tournament_id";
    private static final String COLUMN_PLAYOFF_LIST = "list";
    private static final String COLUMN_PLAYOFF_WINNER_LIST = "winner_list";
    private static final String COLUMN_PLAYOFF_ROUND = "round";

    // Standing columns name
    private static final String COLUMN_STANDING_ID = "id";
    private static final String COLUMN_STANDING_TOURNAMENT_ID = "tournament_id";
    private static final String COLUMN_STANDING_PLAYER_ID = "player_id";
    private static final String COLUMN_STANDING_WINNER = "winner";
    private static final String COLUMN_STANDING_LOSER = "lose";
    private static final String COLUMN_STANDING_POINT = "point";


//    private static final String COLUMN_MATCH_ID = "match_id";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
    // create table sql query for player
    private String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER + "("
            + COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PLAYER_NAME + " TEXT,"
            + COLUMN_PLAYER_TYPE + " TEXT," + COLUMN_PLAYER_DATE + " LONG," + COLUMN_PLAYER_TOURNAMENT_ID + " INTEGER,"
            + COLUMN_PLAYER_DESCRIPTION + " TEXT" + ")";

    private String CREATE_TOURNAMENT_TABLE = "CREATE TABLE " + TABLE_TOURNAMENT + "("
            + COLUMN_TOURNAMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TOURNAMENT_NAME + " TEXT,"
            + COLUMN_TOURNAMENT_TYPE + " TEXT," + COLUMN_TOURNAMENT_GAME_TYPE + " TEXT," + COLUMN_TOURNAMENT_STATUS + " TEXT,"
            + COLUMN_TOURNAMENT_WINNER_ID + " INTEGER," + COLUMN_TOURNAMENT_DATE + " LONG" + ")";

    // create table sql query for king of the hill
    private String CREATE_KOTH_TABLE = "CREATE TABLE " + TABLE_KING_OF_THE_HILL + "("
            + COLUMN_KOTH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KOTH_TOURNAMENT_ID + " INTEGER,"
            + COLUMN_KOTH_LIST + " TEXT," + COLUMN_KOTH_ROUND + " INTEGER, " + COLUMN_KOTH_NUMBER + " INTEGER" + ")";

    // create table sql query for Playoff
    private String CREATE_PLAYOFF_TABLE = "CREATE TABLE " + TABLE_PLAYOFF + "("
            + COLUMN_PLAYOFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PLAYOFF_TOURNAMENT_ID + " INTEGER,"
            + COLUMN_PLAYOFF_LIST + " TEXT," + COLUMN_PLAYOFF_WINNER_LIST + " TEXT," + COLUMN_PLAYOFF_ROUND + " INTEGER" + ")";

    //    Create table sql query for Match
    private String CREATE_MATCH_TABLE = "CREATE TABLE " + TABLE_MATCH + "("
            + COLUMN_MATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MATCH_TOURNAMENT_ID + " INTEGER,"
            + COLUMN_MATCH_PLAYER_1 + " INTEGER," + COLUMN_MATCH_PLAYER_2 + " INTEGER," + COLUMN_MATCH_NUMBER + " INTEGER,"
            + COLUMN_MATCH_POINT_1 + " INTEGER," + COLUMN_MATCH_POINT_2 + " INTEGER," + COLUMN_MATCH_STATUS + " INTEGER,"
            + COLUMN_MATCH_DATE + " LONG," + COLUMN_MATCH_ROUND + " INTEGER," + COLUMN_MATCH_WINNER_ID + " INTEGER" + ")";

    //    Create table sql query for Match
    private String CREATE_STANDING_TABLE = "CREATE TABLE " + TABLE_STANDING + "("
            + COLUMN_STANDING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_STANDING_TOURNAMENT_ID + " INTEGER,"
            + COLUMN_STANDING_PLAYER_ID + " INTEGER,"
            + COLUMN_STANDING_WINNER + " INTEGER," + COLUMN_STANDING_LOSER + " INTEGER," + COLUMN_STANDING_POINT + " INTEGER" + ")";


    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_TOURNAMENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_TOURNAMENT;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PLAYER_TABLE);
        db.execSQL(CREATE_TOURNAMENT_TABLE);
        db.execSQL(CREATE_KOTH_TABLE);
        db.execSQL(CREATE_MATCH_TABLE);
        db.execSQL(CREATE_PLAYOFF_TABLE);
        db.execSQL(CREATE_STANDING_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        String DROP_PLAYER_TABLE = "DROP TABLE IF EXISTS " + TABLE_PLAYER;
        db.execSQL(DROP_PLAYER_TABLE);
        db.execSQL(DROP_TOURNAMENT_TABLE);
        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to create user record
     *
     * @param standing
     */
    public void addStanding(Standing standing) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STANDING_TOURNAMENT_ID, standing.getTournament_id());
        values.put(COLUMN_STANDING_PLAYER_ID, standing.getPlayer_id());
        values.put(COLUMN_STANDING_WINNER, standing.getWin());
        values.put(COLUMN_STANDING_LOSER, standing.getLose());
        values.put(COLUMN_STANDING_POINT, standing.getPoint());
        // Inserting Row
        db.insert(TABLE_STANDING, null, values);
        db.close();
    }

    /**
     * This method is to create match record
     *
     * @param match
     */
    public void addMatch(Match match) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCH_TOURNAMENT_ID, match.getTournament_id());
        values.put(COLUMN_MATCH_PLAYER_1, match.getPlayer1());
        values.put(COLUMN_MATCH_PLAYER_2, match.getPlayer2());
        values.put(COLUMN_MATCH_POINT_1, match.getPoint_1());
        values.put(COLUMN_MATCH_POINT_2, match.getPoint_2());
        values.put(COLUMN_MATCH_WINNER_ID, match.getWinner_id());
        values.put(COLUMN_MATCH_DATE, match.getDate());
        values.put(COLUMN_MATCH_NUMBER, match.getNumber());
        values.put(COLUMN_MATCH_STATUS, match.getStatus());
        values.put(COLUMN_MATCH_ROUND, match.getRound());
        // Inserting Row
        db.insert(TABLE_MATCH, null, values);
        db.close();
    }

    /**
     * This method is to fetch all matches from tournament and return the list of match records
     *
     * @param tournament_id
     * @return list
     */
    public List<Match> getMatchesTournament(int tournament_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Match> list = new ArrayList<Match>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MATCH
                + " WHERE " + COLUMN_MATCH_TOURNAMENT_ID + " = ? ", new String[]{String.valueOf(tournament_id)});
        if (cursor.moveToFirst()) {
            do {
                Match match = new Match();
                match.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_ID))));
                match.setTournament_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_TOURNAMENT_ID))));
                match.setPlayer1(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_PLAYER_1))));
                match.setPlayer2(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_PLAYER_2))));
                match.setPoint_1(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_POINT_1))));
                match.setPoint_2(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_POINT_2))));
                match.setWinner_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_WINNER_ID))));
                match.setDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(COLUMN_MATCH_DATE))));
                match.setNumber(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_NUMBER)));
                match.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_STATUS)));
                match.setRound(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_ROUND)));
                // Adding user record to list
                list.add(match);
            } while (cursor.moveToNext());
            return list;
        }
        return null;
    }

    public Match getMatch(int tournament_id, int number) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MATCH + " WHERE " + COLUMN_MATCH_TOURNAMENT_ID + " = ? "
                + "AND " + COLUMN_MATCH_NUMBER + " = ? ", new String[]{String.valueOf(tournament_id), String.valueOf(number)});
        cursor.moveToFirst();
        Match match = new Match();
        match.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_ID)));
        match.setTournament_id(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_TOURNAMENT_ID)));
        match.setPlayer1(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_PLAYER_1)));
        match.setPlayer2(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_PLAYER_2)));
        match.setPoint_1(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_POINT_1)));
        match.setPoint_2(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_POINT_2)));
        match.setWinner_id(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_WINNER_ID)));
        match.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_STATUS)));
        match.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_MATCH_DATE)));
        match.setNumber(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_NUMBER)));
        return match;
    }

    public List<Match> getMatchRound(int tournament_id, int round) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MATCH + " WHERE " + COLUMN_MATCH_TOURNAMENT_ID + " = ? "
                + "AND " + COLUMN_MATCH_ROUND + " = ? ", new String[]{String.valueOf(tournament_id), String.valueOf(round)});

        List<Match> list = new ArrayList<Match>();
        if (cursor.moveToFirst()) {
            do {
                Match match = new Match();
                match.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_ID)));
                match.setTournament_id(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_TOURNAMENT_ID)));
                match.setPlayer1(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_PLAYER_1)));
                match.setPlayer2(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_PLAYER_2)));
                match.setPoint_1(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_POINT_1)));
                match.setPoint_2(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_POINT_2)));
                match.setWinner_id(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_WINNER_ID)));
                match.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_STATUS)));
                match.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_MATCH_DATE)));
                match.setNumber(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_NUMBER)));
                match.setRound(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCH_ROUND)));
                // Adding match record to list
                list.add(match);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to update match record
     *
     * @param match
     */
    public void updateMatch(Match match) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCH_TOURNAMENT_ID, match.getTournament_id());
        values.put(COLUMN_MATCH_PLAYER_1, match.getPlayer1());
        values.put(COLUMN_MATCH_PLAYER_2, match.getPlayer2());
        values.put(COLUMN_MATCH_POINT_1, match.getPoint_1());
        values.put(COLUMN_MATCH_POINT_2, match.getPoint_2());
        values.put(COLUMN_MATCH_DATE, match.getDate());
        values.put(COLUMN_MATCH_STATUS, match.getStatus());
        values.put(COLUMN_MATCH_WINNER_ID, match.getWinner_id());
        values.put(COLUMN_MATCH_ROUND, match.getRound());

        // updating row
        db.update(TABLE_MATCH, values, COLUMN_MATCH_ID + " = ?",
                new String[]{String.valueOf(match.getId())});
        db.close();
    }

    /**
     * This method to update match record
     *
     * @param standing
     */
    public void updateStanding(Standing standing) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STANDING_PLAYER_ID, standing.getPlayer_id());
        values.put(COLUMN_STANDING_TOURNAMENT_ID, standing.getTournament_id());
        values.put(COLUMN_STANDING_WINNER, standing.getWin());
        values.put(COLUMN_STANDING_LOSER, standing.getLose());
        values.put(COLUMN_STANDING_POINT, standing.getPoint());

        // updating row
        db.update(TABLE_STANDING, values, COLUMN_STANDING_TOURNAMENT_ID + " = ? " + "AND  " + COLUMN_STANDING_PLAYER_ID + " = ?",
                new String[]{String.valueOf(standing.getTournament_id()), String.valueOf(standing.getPlayer_id())});
        db.close();
    }

    /**
     * This method is to create user record
     *
     * @param player
     */
    public void addPlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, player.getName());
        values.put(COLUMN_PLAYER_DATE, player.getDate());
        values.put(COLUMN_PLAYER_TYPE, player.getType());
        values.put(COLUMN_PLAYER_DESCRIPTION, player.getDescription());
        values.put(COLUMN_PLAYER_TOURNAMENT_ID, player.getTournament_id());
        db.insert(TABLE_PLAYER, null, values);
        db.close();
    }


    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<Player> getAllPlayer() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_PLAYER_ID,
                COLUMN_PLAYER_NAME,
                COLUMN_PLAYER_DATE,
                COLUMN_PLAYER_TYPE,
                COLUMN_PLAYER_DESCRIPTION,
                COLUMN_PLAYER_TOURNAMENT_ID
        };
        // sorting orders
        String sortOrder =
                COLUMN_PLAYER_NAME + " ASC";
        List<Player> playerList = new ArrayList<Player>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_PLAYER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_ID))));
                player.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_NAME)));
                player.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_PLAYER_DATE)));
                player.setType(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_TYPE)));
                player.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_DESCRIPTION)));
                player.setTournament_id(cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYER_TOURNAMENT_ID)));
                // Adding user record to list
                playerList.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return playerList;
    }

//
//    /**
//     * This method is to fetch all user and return the list of user records
//     *
//     * @return list
//     */
//    public List<Standing> getAllStanding(int tourn) {
//        // array of columns to fetch
//        String[] columns = {
//                COLUMN_STANDING_ID,
//                COLUMN_STANDING_PLAYER_ID,
//                COLUMN_STANDING_TOURNAMENT_ID,
//                COLUMN_STANDING_WINNER,
//                COLUMN_STANDING_LOSER,
//                COLUMN_STANDING_POINT
//        };
//        // sorting orders
//        String sortOrder =
//                COLUMN_STANDING_POINT+ " ASC";
//        List<Standing> standingList = new ArrayList<Standing>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // query the user table
//        /**
//         * Here query function is used to fetch records from user table this function works like we use sql query.
//         * SQL query equivalent to this query function is
//         * SELECT standing_id, player_id,player_win,player_lose,player_point FROM standing ORDER BY player_point;
//         */
//        Cursor cursor = db.query(TABLE_STANDING, //Table to query
//                columns,    //columns to return
//                null,        //columns for the WHERE clause
//                null,        //The values for the WHERE clause
//                null,       //group the rows
//                null,       //filter by row groups
//                sortOrder); //The sort order
//
//
//        // Traversing through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Standing standing = new Standing();
//                standing.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_STANDING_ID))));
//                standing.setPlayer_id((cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_PLAYER_ID)));
//                standing.setTournament_id(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_TOURNAMENT_ID)));
//                standing.setWin(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_WINNER)));
//                standing.setLose(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_LOSER)));
//                standing.setPoint(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_POINT)));
//                // Adding user record to list
//                standingList.add(standing);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        // return user list
//        return standingList;
//    }

    /**
     * This method is to create user record
     *
     * @param tournament
     */
    public void addTournament(Tournament tournament) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TOURNAMENT_NAME, tournament.getName());
        values.put(COLUMN_TOURNAMENT_DATE, tournament.getDate());
        values.put(COLUMN_TOURNAMENT_TYPE, tournament.getTournament_type());
        values.put(COLUMN_TOURNAMENT_GAME_TYPE, tournament.getGame_type());
        values.put(COLUMN_TOURNAMENT_STATUS, tournament.getStatus());
        values.put(COLUMN_TOURNAMENT_WINNER_ID, tournament.getWinner_id());
        db.insert(TABLE_TOURNAMENT, null, values);
        Log.i("TAGTAG", "instert");
        db.close();
    }

    /**
     * This method is to fetch all tournaments and return the list of tournament records
     *
     * @return list
     */
    public List<Tournament> getAllTournament() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_TOURNAMENT_ID,
                COLUMN_TOURNAMENT_NAME,
                COLUMN_TOURNAMENT_DATE,
                COLUMN_TOURNAMENT_TYPE,
                COLUMN_TOURNAMENT_GAME_TYPE,
                COLUMN_TOURNAMENT_STATUS,
                COLUMN_TOURNAMENT_WINNER_ID
        };
        // sorting orders
        String sortOrder =
                COLUMN_TOURNAMENT_DATE + " ASC";
        List<Tournament> tournamentsList = new ArrayList<Tournament>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_TOURNAMENT, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Tournament tournament = new Tournament();
                tournament.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_ID))));
                tournament.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_NAME)));
                tournament.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_TOURNAMENT_DATE)));
                tournament.setTournament_type(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_TYPE)));
                tournament.setGame_type(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_GAME_TYPE)));
                tournament.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_STATUS)));
                tournament.setWinner_id(cursor.getInt(cursor.getColumnIndex(COLUMN_TOURNAMENT_WINNER_ID)));
                // Adding user record to list
                tournamentsList.add(tournament);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return tournamentsList;
    }


    /**
     * This method to update user record
     *
     * @param player
     */
    public void updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, player.getName());
        values.put(COLUMN_PLAYER_DATE, player.getDate());
        values.put(COLUMN_PLAYER_TYPE, player.getType());
        values.put(COLUMN_PLAYER_DESCRIPTION, player.getDescription());
        values.put(COLUMN_PLAYER_TOURNAMENT_ID, player.getTournament_id());
        // updating row
        db.update(TABLE_PLAYER, values, COLUMN_PLAYER_ID + " = ?",
                new String[]{String.valueOf(player.getId())});
        db.close();
    }

    /**
     * This method to update tournament record
     *
     * @param tournament
     */
    public void updateTournament(Tournament tournament) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TOURNAMENT_NAME, tournament.getName());
        values.put(COLUMN_TOURNAMENT_DATE, tournament.getDate());
        values.put(COLUMN_TOURNAMENT_TYPE, tournament.getTournament_type());
        values.put(COLUMN_TOURNAMENT_GAME_TYPE, tournament.getGame_type());
        values.put(COLUMN_TOURNAMENT_STATUS, tournament.getStatus());
        values.put(COLUMN_TOURNAMENT_WINNER_ID, tournament.getWinner_id());

        // updating row
        db.update(TABLE_TOURNAMENT, values, COLUMN_TOURNAMENT_ID + " = ?",
                new String[]{String.valueOf(tournament.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param player
     */
    public void deletePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_PLAYER, COLUMN_PLAYER_ID + " = ?",
                new String[]{String.valueOf(player.getId())});
        db.close();
    }

    public Player getPlayer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYER + " WHERE " + COLUMN_PLAYER_ID + " = ? ", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        if (cursor != null && (cursor.getCount() > 0)) {
            Player player = new Player();
            player.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYER_ID)));
            player.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_NAME)));
            player.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_PLAYER_DATE)));
            player.setType(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_TYPE)));
            player.setDescription(String.valueOf(cursor.getColumnIndex(COLUMN_PLAYER_DESCRIPTION)));
            player.setTournament_id(cursor.getColumnIndex(COLUMN_PLAYER_TOURNAMENT_ID));
            return player;
        }
        return null;
    }

    public Standing getStanding(int tournament_id, int player_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = new String[] {
                String.valueOf(tournament_id),
                String.valueOf(player_id)
        };
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STANDING + " WHERE "
                + COLUMN_STANDING_TOURNAMENT_ID + " = ? " + "AND " + COLUMN_STANDING_PLAYER_ID
                + " = ?", whereArgs);
        cursor.moveToFirst();
        if (cursor != null && (cursor.getCount() > 0)) {
            Standing standing = new Standing();
            standing.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_ID)));
            standing.setPlayer_id(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_PLAYER_ID)));
            standing.setTournament_id(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_TOURNAMENT_ID)));
            standing.setWin(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_WINNER)));
            standing.setLose(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_LOSER)));
            standing.setPoint(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_POINT)));
            Log.i("TAGTAG", "of course return the shit ");
            return standing;
        }
        Log.i("TAGTAG", " - - - - -");
        return null;
    }

    public List<Standing> getAllStanding(int tournament_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STANDING
                        + " WHERE " + COLUMN_STANDING_TOURNAMENT_ID + " = ? ",
                new String[]{String.valueOf(tournament_id)});
        List<Standing> standings = new ArrayList<>();
        // Traversing through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Standing standing = new Standing();
                standing.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_ID)));
                standing.setPlayer_id(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_PLAYER_ID)));
                standing.setTournament_id(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_TOURNAMENT_ID)));
                standing.setWin(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_WINNER)));
                standing.setLose(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_LOSER)));
                standing.setPoint(cursor.getInt(cursor.getColumnIndex(COLUMN_STANDING_POINT)));
                standings.add(standing);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return standings;
    }

    public Tournament getTournament(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOURNAMENT + " WHERE " + COLUMN_TOURNAMENT_ID + " = ? ", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        Tournament tournament = new Tournament();
        tournament.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TOURNAMENT_ID)));
        tournament.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_NAME)));
        tournament.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_TOURNAMENT_DATE)));
        tournament.setTournament_type(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_TYPE)));
        tournament.setGame_type(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_GAME_TYPE)));
        tournament.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_STATUS)));
        tournament.setWinner_id(cursor.getInt(cursor.getColumnIndex(COLUMN_TOURNAMENT_WINNER_ID)));
        return tournament;
    }

    public Tournament getLastTournament(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOURNAMENT +" ORDER BY " + COLUMN_TOURNAMENT_ID + " DESC LIMIT 1", null);

            cursor.moveToFirst();

        Tournament tournament = new Tournament();
        tournament.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TOURNAMENT_ID)));
        tournament.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_NAME)));
        tournament.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_TOURNAMENT_DATE)));
        tournament.setTournament_type(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_TYPE)));
        tournament.setGame_type(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_GAME_TYPE)));
        tournament.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TOURNAMENT_STATUS)));
        tournament.setWinner_id(cursor.getInt(cursor.getColumnIndex(COLUMN_TOURNAMENT_WINNER_ID)));
        return tournament;
    }

    public void deleteTournament(Tournament tournament) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_TOURNAMENT, COLUMN_TOURNAMENT_ID + " = ?",
                new String[]{String.valueOf(tournament.getId())});
        db.close();
    }


    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    // KING OF THE HILL THING

    /**
     * This method is to create user record
     *
     * @param tournament_id
     * @param jsonPlayer
     * @param round
     * @param number
     */
    public void addKOTH(int tournament_id, String jsonPlayer, int round, int number) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_KOTH_TOURNAMENT_ID, tournament_id);
        values.put(COLUMN_KOTH_LIST, jsonPlayer);
        values.put(COLUMN_KOTH_ROUND, round);
        values.put(COLUMN_KOTH_NUMBER, number);
        db.insert(TABLE_KING_OF_THE_HILL, null, values);
        db.close();
    }

    /**
     * This method is return json String player list
     *
     * @param id
     * @return list
     */
    public List<String> getKOTH(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KING_OF_THE_HILL
                + " WHERE " + COLUMN_KOTH_TOURNAMENT_ID + " = ? ", new String[]{String.valueOf(id)});

        if (cursor != null && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            list.add(cursor.getString(cursor.getColumnIndex(COLUMN_KOTH_LIST)));
            list.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_KOTH_ROUND))));
            list.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_KOTH_NUMBER))));
            return list;
        } else {
            list.add("-1");
            return list;
        }
    }

    /**
     * This method to update KOTH table record
     *
     * @param tournament_id
     * @param player_list
     */
    public void updateKOTH(int tournament_id, String player_list) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_KOTH_TOURNAMENT_ID, tournament_id);
        values.put(COLUMN_KOTH_LIST, player_list);
        // updating row
        db.update(TABLE_KING_OF_THE_HILL, values, COLUMN_KOTH_TOURNAMENT_ID + " = ?",
                new String[]{String.valueOf(tournament_id)});
        db.close();
    }

    // PLAYOFF

    /**
     * This method is to create user record
     *
     * @param tournament_id
     * @param jsonPlayer
     * @param round
     */
    public void addPlayoff(int tournament_id, String jsonPlayer, String winnerPlayer, int round) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYOFF_TOURNAMENT_ID, tournament_id);
        values.put(COLUMN_PLAYOFF_LIST, jsonPlayer);
        values.put(COLUMN_PLAYOFF_WINNER_LIST, winnerPlayer);
        values.put(COLUMN_PLAYOFF_ROUND, round);
        db.insert(TABLE_PLAYOFF, null, values);
        db.close();
    }

    /**
     * This method is return json String player list
     *
     * @param id
     * @return list
     */
    public List<String> getPlayoff(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYOFF
                + " WHERE " + COLUMN_PLAYOFF_TOURNAMENT_ID + " = ? ", new String[]{String.valueOf(id)});
        if (cursor != null && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            list.add(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYOFF_LIST)));
            list.add(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYOFF_WINNER_LIST)));
            list.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYOFF_ROUND))));
            return list;
        } else {
            list.add("-1");
            return list;
        }
    }

    /**
     * This method to update PLAYOFF table record
     *
     * @param tournament_id
     * @param player_list
     * @param round
     */
    public void updatePlayoff(int tournament_id, String player_list, String winner_list, int round) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYOFF_TOURNAMENT_ID, tournament_id);
        values.put(COLUMN_PLAYOFF_LIST, player_list);
        values.put(COLUMN_PLAYOFF_WINNER_LIST, winner_list);
        values.put(COLUMN_PLAYOFF_ROUND, round);
        // updating row
        db.update(TABLE_PLAYOFF, values, COLUMN_PLAYOFF_TOURNAMENT_ID + " = ?",
                new String[]{String.valueOf(tournament_id)});
        db.close();
    }
}