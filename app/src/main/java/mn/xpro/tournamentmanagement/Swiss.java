package mn.xpro.tournamentmanagement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import be.md.swiss.pairing.Round;
import mn.xpro.tournamentmanagement.database.SqliteController;

class Swiss extends AppCompatActivity {
    private static ArrayList<be.md.swiss.Player> players = new ArrayList<>();
    private static SqliteController controller;
    LinearLayout container;
    Tournament tournament;
    be.md.swiss.Tournament tournament_swiss;
    DatabaseHelper databaseHelper;
    List<Player> list_players;
    List<Player> next_winners;
    int round_number = 0;
    boolean check = false;
    static int log(int x, int base) {
        return (int) (Math.log(x) / Math.log(base));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swiss);
        list_players = new ArrayList<>();
        next_winners = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        container = findViewById(R.id.container);
        Bundle value = getIntent().getExtras();
        tournament = databaseHelper.getTournament(value.getInt("tournament_id"));
        controller = new SqliteController(this);
        List<String> check_list = new ArrayList<>();
        check_list = databaseHelper.getPlayoff(tournament.getId());
        if (check_list.get(0).equals("-1")) {
            if (value.getString("check").equals("random")) {
                List<Player> listPlayer = databaseHelper.getAllPlayer();
                for (Player player : listPlayer) {
                    if (player.getTournament_id() == value.getInt("tournament_id")) {
                        list_players.add(player);
                    }
                }
                Collections.shuffle(list_players);
            } else {
                List<Integer> get_ids = value.getIntegerArrayList("list_player");
                for (Integer i : get_ids) {
                    list_players.add(databaseHelper.getPlayer(i));
                }
            }
        } else {
            Type type = new TypeToken<ArrayList<Player>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<Player> finalOutputString = gson.fromJson(check_list.get(0), type);
            ArrayList<Player> winnerOutputString = gson.fromJson(check_list.get(1), type);
            round_number = Integer.parseInt(check_list.get(2));
            if (finalOutputString.size() == 0) {
                next_winners.addAll(finalOutputString);
                list_players.addAll(winnerOutputString);
            } else {
                list_players.addAll(finalOutputString);
                next_winners.addAll(winnerOutputString);
            }
            check = true;
        }
        for (Player p : list_players) {
            be.md.swiss.Player player = be.md.swiss.Player.createPlayerWithFirstnameLastname("", p.getName());
            player.setRating(100);
            players.add(player);
        }

        if(list_players.size() < 2){
            finish();
        }
        tournament_swiss = be.md.swiss.Tournament.createTournament(getRoundNumber(list_players.size()));
        tournament_swiss.setName(tournament.getName());
        tournament_swiss.setPlayers(new HashSet<>(players));

        Round round1 = tournament_swiss.pairNextRound();
        boolean success = controller.insert_new_Tournament(tournament_swiss, round1, getRoundNumber(list_players.size()));
        if (!success) {
//            Toast.makeText(Swiss.this, "ERROR: Tournament name already exist! Try different name", Toast.LENGTH_LONG).show();
        }
        controller.close();
        Intent intent = new Intent(Swiss.this, Rounds.class);
        intent.putExtra("tournament", tournament.getName());
        round1 = null;
        tournament_swiss = null;
        for (be.md.swiss.Player p : players) {
            p.setPoints(0);
        }
        startActivity(intent);
        finish();
    }

    private int getRoundNumber(int size) {
        int s = 1;
        while (size > s) {
            s = s * 2;
        }
        s = s * 4;
        int ret = log(s,2);
        return ret;
    }

}

