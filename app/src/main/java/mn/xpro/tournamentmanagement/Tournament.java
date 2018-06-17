package mn.xpro.tournamentmanagement;

public class Tournament {
    private int id;
    private String name;
    private String tournament_type;
    private String game_type;
    private long date;
    private String status;
    private int winner_id;

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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTournament_type() {
        return tournament_type;
    }

    public void setTournament_type(String tournament_type) {
        this.tournament_type = tournament_type;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getWinner_id() {
        return winner_id;
    }

    public void setWinner_id(int winner_id) {
        this.winner_id = winner_id;
    }
}
