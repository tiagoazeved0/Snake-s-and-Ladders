import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
public class Game {
    private final int WIN_CONDITION = 64;
    private Player player;
    private Player diceRolled;
    private CopyOnWriteArrayList<Player> inGamePlayerList;
    private GameServer gameServer;
    private Map<Integer, Integer> up = new HashMap<>();
    private Map<Integer, Integer> down = new HashMap<>();
    private ACIIArt art;
    {
        up.put(5, 28);
        up.put(14, 31);
        up.put(27, 37);
        up.put(45, 53);
        up.put(52, 59);
        down.put(16, 3);
        down.put(25, 9);
        down.put(34, 20);
        down.put(42, 28);
        down.put(51, 35);
        down.put(55, 44);
        down.put(63, 2);
    }
    Game(GameServer gameServer, CopyOnWriteArrayList inGamePlayerList){
        this.gameServer = gameServer;
        this.inGamePlayerList = inGamePlayerList;
        this.up = new HashMap<>();
        this.down = new HashMap<>();
    }
    public int start() {
        gameServer.broadcast(art.welcome());
        int playerPosition = 0;
        for (int i = 0; i < inGamePlayerList.indexOf(player); i++) {
            playerPosition += playerPosition + player.rollDice();
        }
        if (playerPosition == WIN_CONDITION) {
            gameServer.broadcast(art.victory());
            isWon();
        }
        if (playerPosition == down.get(playerPosition)) {
            playerPosition += down.get(playerPosition);
        }
        if (playerPosition == up.get(playerPosition)) {
            playerPosition += up.get(playerPosition);
        }
        if (playerPosition == 0) {
            System.out.println(Messages.INICIO);
        }
        if (playerPosition == 5) {
            System.out.println(Messages.CASA_5);
        }
        if (playerPosition == 14) {
            System.out.println(Messages.CASA_14);
        }
        if (playerPosition == 16) {
            System.out.println(Messages.CASA_16);
        }
        if (playerPosition == 25) {
            System.out.println(Messages.CASA_25);
        }
        if (playerPosition == 27) {
            System.out.println(Messages.CASA_27);
        }
        if (playerPosition == 36) {
            System.out.println(Messages.CASA_36);
        }
        if (playerPosition == 42) {
            System.out.println(Messages.CASA_42);
        }
        if (playerPosition == 45) {
            System.out.println(Messages.CASA_45);
        }
        if (playerPosition == 51) {
            System.out.println(Messages.CASA_51);
        }
        if (playerPosition == 52) {
            System.out.println(Messages.CASA_52);
        }
        if (playerPosition == 55) {
            System.out.println(Messages.CASA_55);
        }
        if (playerPosition == 63) {
            System.out.println(Messages.CASA_63);
        }
        return playerPosition;
    }
    private boolean isWon() {
        art.victory();
        return true;
    }
}