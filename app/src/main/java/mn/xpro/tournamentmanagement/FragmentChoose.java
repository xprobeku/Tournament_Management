package mn.xpro.tournamentmanagement;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class FragmentChoose extends AppCompatActivity implements InterfaceResponse{
    android.support.v4.app.FragmentManager manager;
    Bundle value;
    DatabaseHelper databaseHelper;
    int tournament_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        value = getIntent().getExtras();
        tournament_id = value.getInt("tournament_id");
//        List<String> string_ids = value.getStringArrayList("ids");
        setContentView(R.layout.fragment_activity);
        FirstFragment firstFragment = new FirstFragment();
        manager = getSupportFragmentManager();
        firstFragment.setArguments(value);
        manager.beginTransaction().replace(R.id.firstFragment, firstFragment, firstFragment.getTag()).commit();
//        SecondFragment secondFragment = new SecondFragment();
//        manager.beginTransaction().replace(R.id.secondFragment, secondFragment, secondFragment.getTag()).commit();
    }

//    @Override
//    public void myresponse(Player player) {
//        Player player1 = new Player();
//        player1.setName("test");
//        FirstFragment firstFragment = new FirstFragment();
//        manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.firstFragment, firstFragment, firstFragment.getTag()).commit();
//    }

    @Override
    public void myresponse(List<Player> player) {
        List<Integer> id = new ArrayList<>();
        for(Player p : player){
            id.add(p.getId());
        }
        Tournament tournament = databaseHelper.getTournament(tournament_id);
        tournament.setStatus("1");
        String name = tournament.getTournament_type();
//        for (Player p : player) {
//            p.setTournament_id(tournament.getId());
//            databaseHelper.updatePlayer(p);
//        }
        Intent intent = null;
            if(name.equals("King of the hill")){
                intent = new Intent(FragmentChoose.this, KingOfTheHill.class);
            }
            else if(name.equals("Playoff")){
                intent = new Intent(FragmentChoose.this, Playoff.class);
            }
            else if(name.equals("Group stage and Playoff")){
                intent = new Intent(FragmentChoose.this, GroupStage.class);
            }
            else{
                intent = new Intent(FragmentChoose.this, Swiss.class);
        }

        tournament.setStatus("1");
        databaseHelper.updateTournament(tournament);
        intent.putExtra("tournament_id", tournament_id);
        intent.putIntegerArrayListExtra("list_player", (ArrayList<Integer>) id);
        intent.putExtra("check", "choose");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    @Override
//    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.start_tournament:
//                int tournament_id = value.getInt("tournament_id");
//                Tournament tournament = databaseHelper.getTournament(tournament_id);
//                if(tournament.getName() == "Playoff"){
//                    Intent intent = new Intent(FragmentChoose.this , Playoff.class);
////                intent.putExtra();
//                    startActivity(intent);
//                }
//
//                break;
//        }
//    }
}
