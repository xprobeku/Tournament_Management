package mn.xpro.tournamentmanagement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
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

public class CreatePlayer extends AppCompatActivity implements View.OnClickListener {

    private AppCompatActivity activity = CreatePlayer.this;
    private RecyclerView recyclerViewPlayers;
    private List<Player> listPlayers;
    private AppCompatButton addPlayer;
    private PlayersRecyclerAdapter playersRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    private AppCompatButton backButton;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);
        getSupportActionBar().hide();

        initViews();
        initListener();
        initObjects();
    }
    /**
     * This method is to initialize listener
     */
    private void initListener() {
        addPlayer.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        recyclerViewPlayers = (RecyclerView) findViewById(R.id.recyclerViewPlayers);
        backButton = (AppCompatButton) findViewById(R.id.btn_back);
        addPlayer = (AppCompatButton) findViewById(R.id.btn_add_player);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listPlayers = new ArrayList<>();
        playersRecyclerAdapter = new PlayersRecyclerAdapter(listPlayers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPlayers.setLayoutManager(mLayoutManager);
        recyclerViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPlayers.setHasFixedSize(true);
        recyclerViewPlayers.setAdapter(playersRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);
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
                listPlayers.clear();
                listPlayers.addAll(databaseHelper.getAllPlayer());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                playersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_add_player:
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreatePlayer.this);
                View mview = getLayoutInflater().inflate(R.layout.add_player_dialog, null);
                mBuilder.setView(mview);
                final AlertDialog dialog = mBuilder.create();
                final AppCompatEditText player_name = (AppCompatEditText) mview.findViewById(R.id.take_player_name);
                final AppCompatSpinner player_type = (AppCompatSpinner) mview.findViewById(R.id.player_type);
                ArrayAdapter<CharSequence> player_adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
                player_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                player_type.setAdapter(player_adapter);
                final AppCompatEditText player_desc = (AppCompatEditText) mview.findViewById(R.id.take_player_description);
                AppCompatButton btn_accert = (AppCompatButton) mview.findViewById(R.id.dialogBtn_add_player);

                btn_accert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!player_name.getText().toString().isEmpty()){
                            Player player = new Player();
                            player.setName(player_name.getText().toString().trim());
                            player.setType(player_type.getSelectedItem().toString().trim());
                            long time = System.currentTimeMillis();
                            player.setDate(time);
                            if(!player_desc.getText().toString().isEmpty())
                                player.setDescription(player_desc.getText().toString().trim());
                            databaseHelper.addPlayer(player);
                            getDataFromSQLite();
                            dialog.dismiss();
                        }
                        else
                            Toast.makeText(CreatePlayer.this, R.string.name_error , Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
