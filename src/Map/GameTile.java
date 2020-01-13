package Map;


public class   GameTile {

    private boolean obstacle;
    private int occupiedByPlayer;


    public GameTile() {

        this.obstacle = false;
        this.occupiedByPlayer = -1;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public void setAsObstacle() {
        this.obstacle = true;
    }

    public boolean isOccupied () {
        return this.occupiedByPlayer != -1;
    }

    public boolean occupy (int player) {
        if (!this.isObstacle() && !this.isOccupied()) {
            this.occupiedByPlayer = player;
            return true;
        }
        return false;
    }

}
