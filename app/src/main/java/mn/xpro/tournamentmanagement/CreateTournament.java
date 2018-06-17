package mn.xpro.tournamentmanagement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xpro on 4/8/2018.
 */

public class CreateTournament extends AppCompatActivity implements View.OnClickListener {

    private AppCompatActivity activity = CreateTournament.this;
    private RecyclerView recyclerViewTournaments;
    private List<Tournament> listTournaments;
    private AppCompatButton addTournament;
    private TournamentsRecyclerAdapter TournamentsRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    private AppCompatButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        getSupportActionBar().setTitle("");

        initViews();
        initListener();
        initObjects();
    }
    /**
     * This method is to initialize listener
     */
    private void initListener() {
        addTournament.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        recyclerViewTournaments = (RecyclerView) findViewById(R.id.recyclerViewTournaments);
        backButton = (AppCompatButton) findViewById(R.id.btn_back);
        addTournament = (AppCompatButton) findViewById(R.id.btn_add_tournament);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listTournaments = new ArrayList<>();
        TournamentsRecyclerAdapter = new TournamentsRecyclerAdapter(listTournaments, getBaseContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTournaments.setLayoutManager(mLayoutManager);
        recyclerViewTournaments.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTournaments.setHasFixedSize(true);
        recyclerViewTournaments.setAdapter(TournamentsRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);
        getDataFromSQLite();
    }

    /**
     * This method is to fetch all Tournament records from SQLite
     */
    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listTournaments.clear();
                listTournaments.addAll(databaseHelper.getAllTournament());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                TournamentsRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_add_tournament:
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateTournament.this);
                View mview = getLayoutInflater().inflate(R.layout.add_tournament_dialog, null);
                mBuilder.setView(mview);
                final AlertDialog dialog = mBuilder.create();
                final AppCompatEditText Tournament_name = (AppCompatEditText) mview.findViewById(R.id.take_tournament_name);
                final AppCompatSpinner tournament_type = (AppCompatSpinner)mview.findViewById(R.id.take_tournament_type);
                final AppCompatSpinner game_type = (AppCompatSpinner)mview.findViewById(R.id.take_game_type);
                ArrayAdapter<CharSequence> tournament_adapter = ArrayAdapter.createFromResource(this, R.array.tournament_type, android.R.layout.simple_spinner_item);
                ArrayAdapter<CharSequence> game_adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
                tournament_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                game_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tournament_type.setAdapter(tournament_adapter);
                game_type.setAdapter(game_adapter);
                AppCompatButton btn_accert = (AppCompatButton) mview.findViewById(R.id.dialogBtn_add_tournament);
                btn_accert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!Tournament_name.getText().toString().isEmpty()){
                            Tournament tournament = new Tournament();
                            tournament.setName(Tournament_name.getText().toString().trim());
                            tournament.setTournament_type(tournament_type.getSelectedItem().toString().trim());
                            tournament.setGame_type(game_type.getSelectedItem().toString().trim());
                            long time = System.currentTimeMillis();
                            tournament.setDate(time);
                            tournament.setStatus("0");
                            tournament.setWinner_id(-1);
                            databaseHelper.addTournament(tournament);
                            getDataFromSQLite();
                            dialog.dismiss();
                            Tournament tour = databaseHelper.getLastTournament();
                            Intent intent = new Intent(CreateTournament.this, ChoosePlayer.class);
                            intent.putExtra("id", tour.getId());
                            intent.putExtra("name", tour.getName());
                            intent.putExtra("game_type", tour.getGame_type());
                            intent.putExtra("tournament_type", tour.getTournament_type());
                            intent.putExtra("date", tour.getDate());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(CreateTournament.this, "NO, YOU CANNOT DID" , Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.btn_back:
                Intent goBack = new Intent(CreateTournament.this, MainActivity.class);
                startActivity(goBack);
                break;
        }
    }
}
