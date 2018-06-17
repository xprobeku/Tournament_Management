package mn.xpro.tournamentmanagement;

public class Match {
    private int id;
    private int tournament_id;
    private int player1;
    private int player2;
    private int point_1;
    private int point_2;
    private int winner_id;
    private int number;
    private int status;
    private long date;
    private int round;
    public Match(){
        tournament_id = player1 = player2 = point_1 = point_2 = winner_id = number = -1 ;
        date = -1;
        status = 0;
        round = 0;
    }
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getWinner_id() {
        return winner_id;
    }

    public void setWinner_id(int winner_id) {
        this.winner_id = winner_id;
    }

    public int getPoint_2() {
        return point_2;
    }

    public void setPoint_2(int point_2) {
        this.point_2 = point_2;
    }

    public int getPoint_1() {
        return point_1;
    }

    public void setPoint_1(int point_1) {
        this.point_1 = point_1;
    }

    public int getPlayer2() {
        return player2;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    public int getPlayer1() {
        return player1;
    }

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public int getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(int tournament_id) {
        this.tournament_id = tournament_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
