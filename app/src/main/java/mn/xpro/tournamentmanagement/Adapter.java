package mn.xpro.tournamentmanagement;

/**
 * Created by xpro on 4/8/2018.
 */


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<User> listUsers;
    public UsersRecyclerAdapter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewName.setText(listUsers.get(position).getName());
        holder.textViewEmail.setText(listUsers.get(position).getEmail());
        holder.textViewPassword.setText(listUsers.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }


    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewEmail;
        public AppCompatTextView textViewPassword;

        public UserViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewEmail = (AppCompatTextView) view.findViewById(R.id.textViewEmail);
            textViewPassword = (AppCompatTextView) view.findViewById(R.id.textViewPassword);
        }
    }
}


class PlayersRecyclerAdapter extends RecyclerView.Adapter<PlayersRecyclerAdapter.PlayersViewHolder> {

    private List<Player> listPlayer;
    public PlayersRecyclerAdapter(List<Player> listPlayer) {
        this.listPlayer = listPlayer;
    }

    @Override
    public PlayersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_recycler, parent, false);

        return new PlayersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayersViewHolder holder, int position) {
        holder.textViewName.setText(listPlayer.get(position).getName());
        holder.textViewType.setText(listPlayer.get(position).getType());
    }

    @Override
    public int getItemCount() {
        Log.v(PlayersRecyclerAdapter.class.getSimpleName(),""+listPlayer.size());
        return listPlayer.size();
    }


    /**
     * ViewHolder class
     */
    public class PlayersViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewType;

        public PlayersViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewType = (AppCompatTextView) view.findViewById(R.id.textViewType);
        }
    }
}

class TournamentsRecyclerAdapter extends RecyclerView.Adapter<TournamentsRecyclerAdapter.TournamentsViewHolder> {

    private List<Tournament> listTournaments;
    private DatabaseHelper databaseHelper;
    private Context context;
    public TournamentsRecyclerAdapter(List<Tournament> listTournaments, Context context) {
        this.context = context;
        this.listTournaments = listTournaments;
    }

    @Override
    public TournamentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tournament_recycler, parent, false);
        databaseHelper = new DatabaseHelper(context);
        return new TournamentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TournamentsViewHolder holder, final int position) {
        holder.textViewName.setText(listTournaments.get(position).getName());
        holder.textViewType.setText(listTournaments.get(position).getTournament_type());
        holder.textViewGameType.setText(listTournaments.get(position).getGame_type());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tournament tournament = databaseHelper.getTournament(listTournaments.get(position).getId());
                if(tournament.getStatus().equals("0")) {
                    Intent intent = new Intent(context, ChoosePlayer.class);
                    intent.putExtra("id", tournament.getId());
                    intent.putExtra("name", tournament.getName());
                    intent.putExtra("game_type", tournament.getGame_type());
                    intent.putExtra("tournament_type", tournament.getTournament_type());
                    intent.putExtra("date", tournament.getDate());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else if(tournament.getStatus().equals("1")){
                    if(tournament.getTournament_type().equals("King of the hill")){
                        // KING OF THE HILL
                        Intent intent = new Intent(context, KingOfTheHill.class);
                        intent.putExtra("tournament_id", tournament.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    else if(tournament.getTournament_type().equals("Swiss system")){
                        // SWISS SYSTEM
                        Intent intent = new Intent(context, Swiss.class);
                        intent.putExtra("tournament_id", tournament.getId());
                        intent.putExtra("check","random");
                        context.startActivity(intent);
                    }
                    else if(tournament.getTournament_type().equals("Group stage and Playoff")){
                        // GROUP STAGE AND PLAYOFF
//                        Intent intent = new Intent(context, GroupAndPlayOff.class);
//                        intent.putExtra("tournament_id", tournament.getId());
//                        context.startActivity(intent);
                    }
                    else if(tournament.getTournament_type().equals("Playoff")){
                        // PLAYOFF
                        Intent intent = new Intent(context, Playoff.class);
                        intent.putExtra("tournament_id", tournament.getId());
                        context.startActivity(intent);
                    }
                }
                else{
                    Intent Congratulation = new Intent(context, Congratulation.class);
                    Congratulation.putExtra("tournament_id", tournament.getId());
                    context.startActivity(Congratulation);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(TournamentsRecyclerAdapter.class.getSimpleName(),""+listTournaments.size());
        return listTournaments.size();
    }

    /**
     * ViewHolder class
     */
    public class TournamentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewType;
        public AppCompatTextView textViewGameType;
        public LinearLayout linearLayout;
        public TournamentsViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewType = (AppCompatTextView) view.findViewById(R.id.textViewType);
            textViewGameType = (AppCompatTextView)view.findViewById(R.id.textViewGameType);
            linearLayout = (LinearLayout) view.findViewById(R.id.linear);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(this, String.valueOf(view.getId()), Toast.LENGTH_LONG).show();
        }
    }
}
//-----------------------------------------------------------------------------------------------------

class ChoosePlayerAdapter extends RecyclerView.Adapter<ChoosePlayerAdapter.ChoosePlayerViewHolder> {

    private List<Player> listPlayer;
    private DatabaseHelper databaseHelper;
    private Context context;
    private List<Integer> player_ids;
    public ChoosePlayerAdapter(List<Player> listPlayer, Context context) {
        this.context = context;
        this.listPlayer = listPlayer;
    }

    @Override
    public ChoosePlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chooser_player, parent, false);
        databaseHelper = new DatabaseHelper(context);
        player_ids = new ArrayList<>();
        return new ChoosePlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChoosePlayerViewHolder holder, final int position) {
        holder.textViewName.setText(listPlayer.get(position).getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player player = databaseHelper.getPlayer(listPlayer.get(position).getId());
                SecondFragment secondFragment = new SecondFragment();
                FragmentManager manager = ((Activity)context).getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                player_ids.add(listPlayer.get(position).getId());
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("ids", (ArrayList<Integer>) player_ids);
                secondFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.secondFragment, secondFragment, secondFragment.getTag()).commit();
                holder.linearLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(TournamentsRecyclerAdapter.class.getSimpleName(),""+listPlayer.size());
        return listPlayer.size();
    }

    /**
     * ViewHolder class
     */
    public class ChoosePlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatTextView textViewName;
        public LinearLayout linearLayout;
        public ChoosePlayerViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            linearLayout = (LinearLayout) view.findViewById(R.id.linear);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(this, String.valueOf(view.getId()), Toast.LENGTH_LONG).show();
        }
    }
}

//-----------------------------------------------------------------------------------------------------

class StandinRankAdapter extends RecyclerView.Adapter<StandinRankAdapter.StandingRankViewHolder> {

    private List<Standing> listStanding;
    private DatabaseHelper databaseHelper;
    private Context context;
    private List<Integer> player_ids;
    public StandinRankAdapter(List<Standing> listStanding, Context context) {
        this.context = context;
        this.listStanding = listStanding;
    }

    @Override
    public StandingRankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standing_rank, parent, false);
        databaseHelper = new DatabaseHelper(context);
        player_ids = new ArrayList<>();
        return new StandingRankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StandingRankViewHolder holder, final int position) {
        holder.textViewName.setText(databaseHelper.getPlayer(listStanding.get(position).getPlayer_id()).getName());
        holder.textViewWin.setText(String.valueOf(listStanding.get(position).getWin()));
        holder.textViewLose.setText(String.valueOf(listStanding.get(position).getLose()));
        holder.textViewPoint.setText(String.valueOf(listStanding.get(position).getPoint()));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAGTAG", "Stading on click1");
//                Player player = databaseHelper.getPlayer(listPlayer.get(position).getId());
//                SecondFragment secondFragment = new SecondFragment();
//                FragmentManager manager = ((Activity)context).getFragmentManager();
//                FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                player_ids.add(listPlayer.get(position).getId());
//                Bundle bundle = new Bundle();
//                bundle.putIntegerArrayList("ids", (ArrayList<Integer>) player_ids);
//                secondFragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.secondFragment, secondFragment, secondFragment.getTag()).commit();
//                holder.linearLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(TournamentsRecyclerAdapter.class.getSimpleName(),""+listStanding.size());
        return listStanding.size();
    }

    /**
     * ViewHolder class
     */
    public class StandingRankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewWin;
        public AppCompatTextView textViewLose;
        public AppCompatTextView textViewPoint;
        public LinearLayout linearLayout;
        public StandingRankViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewWin = (AppCompatTextView) view.findViewById(R.id.textViewWin);
            textViewLose = (AppCompatTextView) view.findViewById(R.id.textViewLose);
            textViewPoint = (AppCompatTextView) view.findViewById(R.id.textViewPoint);
            linearLayout = (LinearLayout) view.findViewById(R.id.linear);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(this, String.valueOf(view.getId()), Toast.LENGTH_LONG).show();
        }
    }
}