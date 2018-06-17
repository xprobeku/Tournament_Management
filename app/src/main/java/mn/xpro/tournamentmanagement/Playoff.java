package mn.xpro.tournamentmanagement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class Playoff extends AppCompatActivity {
    LinearLayout container;
    Tournament tournament;
    DatabaseHelper databaseHelper;
    List<Player> list_players;
    List<Player> next_winners;
    int round_number = 0;
    int current_round_number = 1;
    int play_size = 0;
    int check_play_size = 0;
    int real_size = 0;
    int point1 = -1, point2 = -1, match_count = 0;
    boolean check = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playoff);
        list_players = new ArrayList<>();
        next_winners = new ArrayList<>();
        Button btn_next = (Button) findViewById(R.id.btn_next);
        Button btn_previous = (Button) findViewById(R.id.btn_previous);
        Button btn_exit = (Button) findViewById(R.id.btn_exit);
        Button btn_end = (Button)findViewById(R.id.tournament_end);
        btn_exit.setOnClickListener(ButtonClickListener());
        btn_next.setOnClickListener(ButtonClickListener());
        btn_end.setOnClickListener(ButtonClickListener());
        btn_previous.setOnClickListener(ButtonClickListener());
        databaseHelper = new DatabaseHelper(this);
        container = findViewById(R.id.container);
        Bundle value = getIntent().getExtras();
        tournament = databaseHelper.getTournament(value.getInt("tournament_id"));
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
            }
            else{
                List<Integer> get_ids = value.getIntegerArrayList("list_player");
                for(Integer i : get_ids){
                    list_players.add(databaseHelper.getPlayer(i));
                }
            }
        }
        else{
            Type type = new TypeToken<ArrayList<Player>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Player> finalOutputString =  gson.fromJson(check_list.get(0), type);
            ArrayList<Player> winnerOutputString = gson.fromJson(check_list.get(1), type);
            round_number = Integer.parseInt(check_list.get(2));
            if(finalOutputString.size() == 0){
                next_winners.addAll(finalOutputString);
                list_players.addAll(winnerOutputString);
            }
            else{
                list_players.addAll(finalOutputString);
                next_winners.addAll(winnerOutputString);
            }
            check = true;
        }

        // Take list player , and round take it you may have some issue, get database playoff information and make it
        // if there have nothing saved, go make new player list, you can do it. You have to do on 2th April

        // Match zurah
        int lenght = list_players.size();
        int count = databaseHelper.getAllStanding(tournament.getId()).size();
        if(count == 0)
            for(Player player : list_players){
                Standing standing = new Standing();
                standing.setTournament_id(tournament.getId());
                standing.setPoint(0);
                standing.setWin(0);
                standing.setLose(0);
                standing.setPlayer_id(player.getId());
                databaseHelper.addStanding(standing);
            }
        drawMatch(lenght);
        int gg = getMatchNumber(list_players.size());
        int tt = (gg % 2 == 0) ? gg : gg + 1;
        check_play_size = tt;
        if(next_winners.size() == 0)
            real_size = list_players.size();
        else
            real_size = list_players.size() + 2*next_winners.size();
    }

    private View.OnClickListener ButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_next:
                        int ch = list_players.size();
                        int cha = next_winners.size();
                        if (ch == 1){
                            next_winners.add(list_players.get(0));
                            Standing standing = databaseHelper.getStanding(tournament.getId(),list_players.get(0).getId());
                            int win = standing.getWin() + 1;
                            int point = standing.getPoint() + 3;
                            standing.setWin(win);
                            standing.setPoint(point);
                            databaseHelper.updateStanding(standing);
                            removeAll(list_players);
                        }
                        if (cha < real_size - check_play_size / 2) {
                            Toast.makeText(Playoff.this, " no", Toast.LENGTH_SHORT).show();
                        } else {
                            real_size -= check_play_size/2;
                            check_play_size /= 2;
                            list_players.addAll(next_winners);
                            removeAll(next_winners);
                            drawMatch(check_play_size);
                            current_round_number++;
                        }
                        break;
                    case R.id.btn_previous:

                        break;
                    case R.id.tournament_end:
                        Intent end = new Intent(Playoff.this, MainActivity.class);
                        startActivity(end);
                        break;
                    case R.id.btn_exit:
                        if (list_players.size() == 1){
                            next_winners.add(list_players.get(0));
                            removeAll(list_players);
                        }
                        Gson gson = new Gson();
                        String arrayList = gson.toJson(list_players);
                        String winnerList = gson.toJson(next_winners);
                        if(!check)
                            databaseHelper.addPlayoff(tournament.getId(), arrayList, winnerList, round_number);
                        else
                            databaseHelper.updatePlayoff(tournament.getId(), arrayList, winnerList,round_number);
                        Intent exit = new Intent(Playoff.this, MainActivity.class);
                        startActivity(exit);
                        break;
                }
            }
        };
    }


    private void createMatch() {
        int match_number = getMatchNumber(list_players.size());
        int check = (match_number % 2 == 0) ? match_number : match_number + 1;
        play_size = check;
        for (int i = 0; i < match_number; i++) {
            Match match = new Match();
            match.setTournament_id(tournament.getId());
            match.setNumber(i);
            match.setStatus(0);
            int round = return_round(check, i + 1);
            match.setRound(round);
            databaseHelper.addMatch(match);
        }
    }

    // Rounds дугаар буцаадаг функц
    private int return_round(int lenght, int match_number) {
        int count = 0;
        int k = lenght;
        int s = 0;
        while (k != 1) {
            s = s + k / 2;
            k /= 2;
            count++;
            if (match_number <= s) {
                return count;
            }
        }
        return count;
    }

    @SuppressLint("ResourceAsColor")
    private void drawMatch(int lenght) {
        if (container.getChildCount() > 0) {
            container.removeAllViewsInLayout();
        }
        if (list_players.size() == 1 && next_winners.size() == 0) {
            Intent congratulation = new Intent(Playoff.this, Congratulation.class);
            congratulation.putExtra("tournament_id", tournament.getId());
            tournament.setWinner_id(list_players.get(0).getId());
            databaseHelper.updateTournament(tournament);
//            congratulation.putExtra("winner", list_players.get(0).getId());
            startActivity(congratulation);
            return;
        }
        for (int i = 0; i < list_players.size(); i += 2) {
            LinearLayout contain = new LinearLayout(this);
            contain.setId(i);
            LinearLayout subcontainer_1 = new LinearLayout(this);
            LinearLayout subcontainer_2 = new LinearLayout(this);

            TextView player1 = new TextView(this);
            TextView player2 = new TextView(this);
            TextView point_1 = new TextView(this);
            TextView point_2 = new TextView(this);

            subcontainer_1.setOrientation(LinearLayout.HORIZONTAL);
            subcontainer_2.setOrientation(LinearLayout.HORIZONTAL);
            contain.setOrientation(LinearLayout.VERTICAL);
            Drawable mDrawable = ContextCompat.getDrawable(this, R.drawable.custom_border);
            mDrawable.setColorFilter(new PorterDuffColorFilter(R.color.colorTextHint, PorterDuff.Mode.MULTIPLY));
            contain.setBackground(mDrawable);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 0, 30, 30);
            LinearLayout.LayoutParams subParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            subParam.setMargins(0, 0, 30, 5);
            player1.setText(list_players.get(i).getName());
            player1.setTextSize(25);
            player1.setId(list_players.get(i).getId());
            point_1.setText("0");
            point_1.setTextSize(25);
            point_1.setId(10000 + list_players.get(i).getId());
            point_1.setGravity(Gravity.RIGHT);
            if (i + 1 == lenght && (lenght % 2) != 0) {
                subcontainer_1.addView(player1);
                subcontainer_1.addView(point_1, subParam);
            } else {
                player2.setText(list_players.get(i + 1).getName());
                player2.setTextSize(25);
                player2.setId(list_players.get(i + 1).getId());
                point_2.setText("0");
                point_2.setTextSize(25);
                point_2.setId(10000 + list_players.get(i + 1).getId());
                point_2.setGravity(Gravity.RIGHT);
                subcontainer_1.addView(player1);
                subcontainer_1.addView(point_1, subParam);
                subcontainer_2.addView(player2);
                subcontainer_2.addView(point_2, subParam);
                contain.setOnClickListener(onClickListener());
            }
            contain.addView(subcontainer_1);
            contain.addView(subcontainer_2);
            container.addView(contain);
        }
    }

    private int getMatchNumber(int size) {
        int lenght = (size % 2 == 0) ? size : size + 1;
        int round = lenght;
        int n = 0;
        while (round != 1) {
            n += round / 2;
            round = round / 2;
            round_number++;
        }
        return n;
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            View container_view;

            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                {
                    container_view = view;
                    final List<Integer> player_ids = new ArrayList<>();
                    final List<Integer> point_ids = new ArrayList<>();
                    for (int i = 0; i < container.getChildCount(); i++) {
                        View nextChild = container.getChildAt(i);
                        if (nextChild instanceof LinearLayout) {
                            LinearLayout layout = (LinearLayout) nextChild;
                            if (layout.getId() == view.getId()) {
                                for (int j = 0; j < layout.getChildCount(); j++) {
                                    View child_layout = layout.getChildAt(j);
                                    if (child_layout instanceof LinearLayout) {
                                        LinearLayout inner_child_layout = (LinearLayout) child_layout;
                                        for (int k = 0; k < inner_child_layout.getChildCount(); k++) {
                                            View children = inner_child_layout.getChildAt(k);
                                            if (children instanceof TextView) {
                                                TextView tv = (TextView) children;
                                                if (tv.getId() < 10000) {
                                                    player_ids.add(tv.getId());
                                                } else {
                                                    point_ids.add(tv.getId());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Playoff.this);
                    View mview = getLayoutInflater().inflate(R.layout.add_match_result_dialog, null);
                    mBuilder.setView(mview);
                    final AlertDialog dialog = mBuilder.create();
                    final TextView player1_name = (TextView) mview.findViewById(R.id.tv_player_1_name);
                    final TextView player2_name = (TextView) mview.findViewById(R.id.tv_player_2_name);
                    final Player player1 = databaseHelper.getPlayer(player_ids.get(0));
                    Player player2 = databaseHelper.getPlayer(player_ids.get(1));

                    player1_name.setText(player1.getName());
                    player2_name.setText(player2.getName());

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
                            int winner = 0, loser = 0;
                            point1 = Integer.valueOf(String.valueOf(et_player1_point.getText()));
                            point2 = Integer.valueOf(String.valueOf(et_player2_point.getText()));
//                            Match match = databaseHelper.getMatch(tournament.getId(), container_view.getId());
                            Match match = new Match();
                            match.setTournament_id(tournament.getId());
                            match.setPlayer1(player_ids.get(0));
                            match.setPlayer2(player_ids.get(1));
                            match.setPoint_1(point1);
                            match.setPoint_2(point2);
                            long time = System.currentTimeMillis();
                            match.setDate(time);
                            match.setStatus(1);
                            match.setRound(current_round_number);

                            if (point1 > point2) {
                                match.setWinner_id(player_ids.get(0));
                                winner = player_ids.get(0);
                                loser = player_ids.get(1);
                                next_winners.add(databaseHelper.getPlayer(player_ids.get(0)));
                                list_players.remove(databaseHelper.getPlayer(player_ids.get(1)));
                                list_players.remove(databaseHelper.getPlayer(player_ids.get(0)));
                                for (Iterator<Player> iter = list_players.listIterator(); iter.hasNext(); ) {
                                    Player a = iter.next();
                                    if (a.getId() == player_ids.get(1) || a.getId() == player_ids.get(0)) {
                                        iter.remove();
                                    }
                                }

                                databaseHelper.addMatch(match);
                                container_view.setOnClickListener(null);
                            } else if (point2 > point1) {
                                match.setWinner_id(player_ids.get(1));
                                winner = player_ids.get(1);
                                loser = player_ids.get(0);
                                next_winners.add(databaseHelper.getPlayer(player_ids.get(1)));
                                for (Iterator<Player> iter = list_players.listIterator(); iter.hasNext(); ) {
                                    Player a = iter.next();
                                    if (a.getId() == player_ids.get(0) || a.getId() == player_ids.get(1)) {
                                        iter.remove();
                                    }
                                }
                                databaseHelper.addMatch(match);
                                container_view.setOnClickListener(null);
                            } else
                                Toast.makeText(Playoff.this, "Cant draw", Toast.LENGTH_SHORT).show();
                            if(point1 != point2){
                                TextView p_1 = (TextView) container_view.findViewById(point_ids.get(0));
                                TextView p_2 = (TextView) container_view.findViewById(point_ids.get(1));
                                p_1.setText(String.valueOf(point1));
                                p_2.setText(String.valueOf(point2));
                                dialog.dismiss();
                                Log.i("TAGTAG", "--------------" + winner);
                                Standing standing = databaseHelper.getStanding(tournament.getId(), winner);
                                Log.i("TAGTAG", standing.getPlayer_id() + " player_id");
                                int win = standing.getWin() + 1;
                                int point = standing.getPoint() + 3;
                                standing.setWin(win);
                                standing.setPoint(point);
                                databaseHelper.updateStanding(standing);
                                Standing standing1 = databaseHelper.getStanding(tournament.getId(), loser);
                                int lose = standing1.getLose() + 1;
                                standing1.setLose(lose);
                                databaseHelper.updateStanding(standing1);
                            }
                        }
                    });
                    cancel_point.setOnClickListener(new View.OnClickListener() {
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
            }
        };
    }
    public void removeAll(List<Player> player) {
        for (Iterator<Player> iter = player.listIterator(); iter.hasNext(); ) {
            iter.next();
            iter.remove();
        }
    }

}

