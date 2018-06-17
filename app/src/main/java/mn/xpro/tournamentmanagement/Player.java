package mn.xpro.tournamentmanagement;

import java.sql.Date;

/**
 * Created by xpro on 4/9/2018.
 */

public class Player {
    private int id;
    private String name;
    private String type;
    private long date;
    private String description;
    private int tournament_id;
    public Player(){
        id = -1;
        name = "unknown";
        type = "unknown";
        date = 0;
        description = "none";
        tournament_id = -1;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(int tournament_id) {
        this.tournament_id = tournament_id;
    }
}
