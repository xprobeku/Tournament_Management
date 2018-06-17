package mn.xpro.tournamentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

class Congratulation extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    Bundle value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);
        value = getIntent().getExtras();
        LinearLayout layout = (LinearLayout) findViewById(R.id.winner_layout);
        layout.setOnClickListener(onClickListener());
        databaseHelper = new DatabaseHelper(this);
//        Player winner = databaseHelper.getPlayer(value.getInt("winner"));
        int tournament_id = value.getInt("tournament_id");
        Tournament tournament = databaseHelper.getTournament(tournament_id);
        tournament.setStatus("-1");
        Player winner = databaseHelper.getPlayer(tournament.getWinner_id());
        databaseHelper.updateTournament(tournament);
        TextView tf = (TextView) findViewById(R.id.winner);
        tf.setText(winner.getName());
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Congratulation.this, ClosedTournament.class);
                back.putExtra("tournament_id",value.getInt("tournament_id"));
                startActivity(back);
            }
        };
    }
}
