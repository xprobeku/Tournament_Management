package mn.xpro.tournamentmanagement;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class ChoosePlayer extends AppCompatActivity {
    LinearLayout my_layout;
    DatabaseHelper databaseHelper;
    AppCompatButton btn_start_tournament;
    Bundle data;
    Tournament tournament;
    List<Player> players;
    private ArrayList<String> AllCheckbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_player);
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();

        for (Player player : players) {
            if (data.getString("game_type").equals(player.getType())) {
                CheckBox checkBox = new CheckBox(this);
//                checkBox.setOnClickListener(getOnClickListener(checkBox));
                checkBox.setId(player.getId());
                checkBox.setText(player.getName());
                checkBox.setChecked(true);
                my_layout.addView(checkBox);
            }
        }
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(this);
        AllCheckbox = new ArrayList<>();
        tournament = databaseHelper.getTournament(data.getInt("id"));
        players = databaseHelper.getAllPlayer();
    }

    private void initListeners() {
        btn_start_tournament.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                AllCheckbox.clear();
                for (int i = 0; i < my_layout.getChildCount(); i++) {
                    View nextChild = my_layout.getChildAt(i);
                    if (nextChild instanceof CheckBox) {
                        CheckBox check = (CheckBox) nextChild;
                        if (check.isChecked()) {
                            AllCheckbox.add(String.valueOf(check.getId()));
                        }
                    }
                }
                String[] array = getResources().getStringArray(R.array.tournament_type);
                String type = tournament.getTournament_type();
                if (type.equals(array[0])) {
                    // swiss system
                    if(AllCheckbox.size() < 6){
                        Toast.makeText(ChoosePlayer.this, "Add more player, minimum player is 6", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent swiss = new Intent(ChoosePlayer.this, Swiss.class);
                        for (String id : AllCheckbox) {
                            Player player = databaseHelper.getPlayer(Integer.valueOf(id));
                            player.setTournament_id(tournament.getId());
                            databaseHelper.updatePlayer(player);
                        }
                        tournament.setStatus("1");
                        databaseHelper.updateTournament(tournament);
                        swiss.putExtra("tournament_id", tournament.getId());
                        swiss.putExtra("check", "random");
                        startActivity(swiss);
                    }

                } else if (type.equals(array[1])) {
                    if (AllCheckbox.size() < 3)
                        Toast.makeText(ChoosePlayer.this, "Add more player, minimum player 3", Toast.LENGTH_SHORT).show();
                    else {
                        //King of the hill
                        Intent king = new Intent(ChoosePlayer.this, KingOfTheHill.class);
                        for (String id : AllCheckbox) {
                            Player player = databaseHelper.getPlayer(Integer.valueOf(id));
                            player.setTournament_id(tournament.getId());
                            databaseHelper.updatePlayer(player);
                        }
                        tournament.setStatus("1");
                        databaseHelper.updateTournament(tournament);
                        king.putExtra("tournament_id", tournament.getId());
                        startActivity(king);
                    }
                } else if (type.equals(array[2])) {
                    // Group stage and Playoff
                    for (String id : AllCheckbox) {
                        Player player = databaseHelper.getPlayer(Integer.valueOf(id));
                        player.setTournament_id(tournament.getId());
                        databaseHelper.updatePlayer(player);
                    }

                } else if (type.equals(array[3])) {
                    // Playoff
                    for (String id : AllCheckbox) {
                        Player player = databaseHelper.getPlayer(Integer.valueOf(id));
                        player.setTournament_id(tournament.getId());
                        databaseHelper.updatePlayer(player);
                    }
                    if (AllCheckbox.size() % 2 == 0 && AllCheckbox.size() <= 32){
                        if((AllCheckbox.size() / 2) % 2 == 0){
                            // PLAYOFF
                            startPlayOff();
                        }
                        else
                            Toast.makeText(ChoosePlayer.this, "Add more player, minimum player 2", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(AllCheckbox.size() <= 32 && ((AllCheckbox.size() + 1) / 2) % 2 == 0){
                            // PLAYOFF
                            startPlayOff();
                        }
                        else
                            Toast.makeText(ChoosePlayer.this, "Can't create tournament, minimum 2 , maximum 32", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            private void startPlayOff() {
                AlertDialog.Builder altdial = new AlertDialog.Builder(ChoosePlayer.this);
                altdial.setMessage("What you want to do ??? ").setCancelable(false)
                        .setPositiveButton("Custom", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ChoosePlayer.this, FragmentChoose.class);
                                intent.putExtra("tournament_id", tournament.getId());
                                intent.putExtra("ids", AllCheckbox);
                                startActivity(intent);

                            }
                        }).setNegativeButton("Random", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Please", "they choose random dude what ever you want do it");
                        //Playoff
                        Intent playoff = new Intent(ChoosePlayer.this, Playoff.class);

                        for (String id : AllCheckbox) {
                            Player player = databaseHelper.getPlayer(Integer.valueOf(id));
                            player.setTournament_id(tournament.getId());
                            databaseHelper.updatePlayer(player);
                        }
                        tournament.setStatus("1");
                        databaseHelper.updateTournament(tournament);
                        playoff.putExtra("tournament_id", tournament.getId());
                        playoff.putExtra("check", "random");
                        startActivity(playoff);
                    }
                });
                AlertDialog alert = altdial.create();
                alert.setTitle("Dialog HEADER what you want");
                alert.show();

                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.BLACK);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.BLACK);
            }
        });
    }

    private void initViews() {
        btn_start_tournament = (AppCompatButton) findViewById(R.id.btn_start_tournament);
        my_layout = (LinearLayout) findViewById(R.id.layout_choose_player);
        data = getIntent().getExtras();
    }

//    View.OnClickListener getOnClickListener(final Button checkBox) {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("Please", "Check - id" + checkBox.getId() + "Text: " + checkBox.getText().toString());
//            }
//        };
//    }
}
