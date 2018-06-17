package mn.xpro.tournamentmanagement;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ClosedTournament extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewRank;
    private StandinRankAdapter standinRankAdapter;
    List<Standing> listStanding ;
    int tournament_id ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout_status);
        recyclerViewRank = (RecyclerView)findViewById(R.id.recyclerViewRank);
        Bundle value = getIntent().getExtras();
        listStanding = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        tournament_id = value.getInt("tournament_id");
        List<Match> match = new ArrayList<Match>();
        match.addAll(databaseHelper.getMatchesTournament(tournament_id));
        Log.i("TAGTAG", match.size() + " size of match");
        if(match != null)
            for (Match m : match) {
                    Log.i("Please", databaseHelper.getPlayer(m.getPlayer1()).getName() + " - "
                            + m.getPoint_1()
                            + " VS "
                            + databaseHelper.getPlayer(m.getPlayer2()).getName() + " - "
                            + m.getPoint_2() + " = ----------------------------  round: " + m.getRound());
        }
        else{
            finish();
        }
        Log.i("TAGTAG", match.size() + " size of match ---------- ");
        Collections.sort(match, new Comparator<Match>(){
            @Override
            public int compare(Match match, Match t1) {
                return match.getRound() - t1.getRound();
            }
        });
        for (Match m : match) {
            Log.i("Please", databaseHelper.getPlayer(m.getPlayer1()).getName() + " - "
                    + m.getPoint_1()
                    + " VS "
                    + databaseHelper.getPlayer(m.getPlayer2()).getName() + " - "
                    + m.getPoint_2() + " = ----------------------------  round: " + m.getRound());
        }
        Set<Integer> players = new HashSet<>();
        Set<Integer> player2 = new  HashSet<>();
        for(Match m : match){
            players.add(m.getPlayer1());
            player2.add(m.getPlayer2());
        }
        players.remove(player2);
        List<Player> all_player = new ArrayList<>();
        for(Integer a : players){
            Player p = databaseHelper.getPlayer(a);
            all_player.add(p);
        }

        List<Standing> standings = databaseHelper.getAllStanding(tournament_id);
        for(Standing standing: standings){
            System.out.println(standing.getPlayer_id());
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        standinRankAdapter = new StandinRankAdapter(listStanding, getBaseContext());
        recyclerViewRank.setLayoutManager(mLayoutManager);
        recyclerViewRank.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRank.setHasFixedSize(true);
        recyclerViewRank.setAdapter(standinRankAdapter);
        databaseHelper = new DatabaseHelper(this);
        getDataFromSQLite();
    }


    /**
     * This method is to fetch all Player records from SQLite
     */
    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listStanding.clear();
                List<Standing> standings = databaseHelper.getAllStanding(tournament_id);
                Collections.sort(standings, new Comparator<Standing>() {
                    @Override
                    public int compare(Standing standing, Standing t1) {
                        return String.valueOf(t1.getPoint()).compareToIgnoreCase(String.valueOf(standing.getPoint()));
                    }
                });
                listStanding.addAll(standings);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                standinRankAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
