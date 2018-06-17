package mn.xpro.tournamentmanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import java.util.List;

class KingOfTheHill extends AppCompatActivity {
    List<Player> list_players;
    DatabaseHelper databaseHelper;
    TextView player1, player2, nextplayer;
    LinearLayout layout;
    Button btn_exit, btn_tournament_end;
    int point_1, point_2, tournament_id, round , number, player_lenght = 0;
    boolean check = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.king_of_the_hill);
        databaseHelper = new DatabaseHelper(this);
        list_players = new ArrayList<Player>();
        player1 = (TextView) findViewById(R.id.player_1);
        player2 = (TextView) findViewById(R.id.player_2);
        nextplayer = (TextView) findViewById(R.id.next_player);
        layout = (LinearLayout) findViewById(R.id.linear_layout_king_match);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_tournament_end = (Button) findViewById(R.id.tournament_end);
        final Bundle value = getIntent().getExtras();
        List<String> check_list = new ArrayList<>();
        tournament_id = value.getInt("tournament_id");
        check_list = databaseHelper.getKOTH(tournament_id);
        if (!check_list.get(0).equals("-1")) {
            Type type = new TypeToken<ArrayList<Player>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Player> finalOutputString =  gson.fromJson(check_list.get(0), type);
            round = Integer.parseInt(check_list.get(1));
            number = Integer.parseInt(check_list.get(2));

            for(Player a : finalOutputString){
                list_players.add(a);
            }
            check = true;
        } else {
            List<Player> listPlayer = databaseHelper.getAllPlayer();
            number  = 1;
            round = 1;
            for (Player player : listPlayer) {
                if (player.getTournament_id() == tournament_id) {
                    list_players.add(player);
                }
            }
        }
        player_lenght = list_players.size();
        // Make standing
        int count = databaseHelper.getAllStanding(tournament_id).size();
        if(count == 0)
            for(Player player : list_players){
                Standing standing = new Standing();
                standing.setTournament_id(tournament_id);
                standing.setPoint(0);
                standing.setWin(0);
                standing.setLose(0);
                standing.setPlayer_id(player.getId());
                databaseHelper.addStanding(standing);
            }
        nextMatch();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(KingOfTheHill.this);
                View mview = getLayoutInflater().inflate(R.layout.add_match_result_dialog, null);
                mBuilder.setView(mview);
                final AlertDialog dialog = mBuilder.create();
                final TextView player1_name = (TextView) mview.findViewById(R.id.tv_player_1_name);
                final TextView player2_name = (TextView) mview.findViewById(R.id.tv_player_2_name);
                player1_name.setText(list_players.get(0).getName());
                player2_name.setText(list_players.get(1).getName());
                AppCompatButton player1_minus = (AppCompatButton) mview.findViewById(R.id.player_1_minus);
                Button player2_minus = (Button) mview.findViewById(R.id.player_2_minus);
                Button player1_plus = (Button) mview.findViewById(R.id.player_1_plus);
                Button player2_plus = (Button) mview.findViewById(R.id.player_2_plus);
                final EditText et_player1_point = (EditText) mview.findViewById(R.id.et_player1_point);
                final EditText et_player2_point = (EditText) mview.findViewById(R.id.et_player2_point);
                final Button accept_point = (Button) mview.findViewById(R.id.accert_point);
                final Button cancel_point = (Button) mview.findViewById(R.id.cancel_point);
                accept_point.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int player_id_1 = 0, player_id_2 = 0;
                        point_1 = Integer.valueOf(String.valueOf(et_player1_point.getText()));
                        point_2 = Integer.valueOf(String.valueOf(et_player2_point.getText()));
                        Match match = new Match();
                        match.setTournament_id(tournament_id);
                        match.setPlayer1(list_players.get(0).getId());
                        match.setPlayer2(list_players.get(1).getId());
                        match.setPoint_1(point_1);
                        match.setPoint_2(point_2);
                        long time = System.currentTimeMillis();
                        match.setDate(time);
                        if(number < player_lenght - 1){
                            number++;
                        }
                        else{
                            number = 1;
                            round++;
                        }
                        if (point_1 > point_2) {
                            match.setWinner_id(list_players.get(0).getId());
                            player_id_1 = list_players.get(0).getId();
                            player_id_2 = list_players.get(1).getId();
                            databaseHelper.addMatch(match);
                            list_players.add(list_players.get(1));
                            list_players.remove(1);
                            nextMatch();
                            dialog.dismiss();
                        } else if (point_2 > point_1) {
                            match.setWinner_id(list_players.get(1).getId());
                            player_id_1 = list_players.get(1).getId();
                            player_id_2 = list_players.get(0).getId();
                            databaseHelper.addMatch(match);
                            list_players.add(list_players.get(0));
                            list_players.remove(0);
                            nextMatch();
                            dialog.dismiss();
                        } else
                            Toast.makeText(KingOfTheHill.this, "Cannot draw", Toast.LENGTH_SHORT).show();
                        if(point_1 != point_2){
                            // winner standing update
                            Standing standing = databaseHelper.getStanding(tournament_id, player_id_1);
                            int win = standing.getWin() + 1;
                            standing.setWin(win);
                            int point = standing.getPoint() + 3;
                            standing.setPoint(point);
                            databaseHelper.updateStanding(standing);

                            // loser standing update
                            Standing standing1 = databaseHelper.getStanding(tournament_id, player_id_2);
                            int lose = standing1.getLose() + 1;
                            standing1.setLose(lose);
                            databaseHelper.updateStanding(standing1);

                        }

                    }
                });
                cancel_point.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                player1_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int point_1 = Integer.parseInt(String.valueOf(et_player1_point.getText()));
                        if (point_1 > 0) {
                            point_1--;
                            et_player1_point.setText(String.valueOf(point_1));
                        }
                    }
                });
                player2_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int point_2 = Integer.parseInt(String.valueOf(et_player2_point.getText()));
                        if (point_2 > 0)
                            point_2--;
                        et_player2_point.setText(String.valueOf(point_2));
                    }
                });
                player1_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int point_1 = Integer.parseInt(String.valueOf(et_player1_point.getText()));
                        point_1++;
                        et_player1_point.setText(String.valueOf(point_1));
                    }
                });
                player2_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int point_2 = Integer.parseInt(String.valueOf(et_player2_point.getText()));
                        point_2++;
                        et_player2_point.setText(String.valueOf(point_2));
                    }
                });
                dialog.show();
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String arrayList = gson.toJson(list_players);
                if(!check)
                    databaseHelper.addKOTH(tournament_id, arrayList, round, number);
                else
                    databaseHelper.updateKOTH(tournament_id,arrayList);
                Intent goBack = new Intent(KingOfTheHill.this, CreateTournament.class);
                startActivity(goBack);
            }
        });
        btn_tournament_end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               if(round < 2){
                   Toast.makeText(KingOfTheHill.this , " No you cant" , Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(KingOfTheHill.this , " Yes you can" , Toast.LENGTH_SHORT).show();
                   Tournament tournament = databaseHelper.getTournament(tournament_id);
                   tournament.setStatus("-1");
                   tournament.setWinner_id(list_players.get(0).getId());
                   databaseHelper.updateTournament(tournament);
                   Intent goBack = new Intent(KingOfTheHill.this, CreateTournament.class);
                   startActivity(goBack);
               }
            }
        });
    }

    private void nextMatch() {
        player1.setText(String.valueOf(list_players.get(0).getName()));
        player1.setTextSize(25);
        player2.setText(list_players.get(1).getName());
        player2.setTextSize(25);
        nextplayer.setText(list_players.get(2).getName());
    }
}
