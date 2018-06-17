package mn.xpro.tournamentmanagement;

public class Standing {
    private int id;
    private int player_id;
    private int tournament_id;
    private int win;
    private int lose;
    private int point;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(int tournament_id) {
        this.tournament_id = tournament_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }
}
