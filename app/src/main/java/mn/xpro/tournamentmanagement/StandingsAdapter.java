package mn.xpro.tournamentmanagement;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class StandingsAdapter<String> extends ArrayAdapter {

    private List<String> players;
    private int layoutResourceId;
    private Context context;


    public StandingsAdapter(Context context, int layoutResourceId, List<String> players) {
        super(context, layoutResourceId, players);
        this.context = context;
        this.players = players;
    }

}
