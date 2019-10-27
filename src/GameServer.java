import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {

    private CopyOnWriteArrayList<Player> playersList;
    private ServerSocket serverSocket;
    private BufferedReader in;
    private Player player;
    private ACIIArt art;
    private Game game;
    private int players;
    private int port;
    private volatile boolean turn;

    public GameServer(int port, int players){
        this.port = port;
        this.players = players;
        this.playersList = new CopyOnWriteArrayList<>();
        turn = false;
    }

    public void listen() {
        System.out.println("======================== Server initiated ========================\n");

        System.out.println(art.welcome());
        try {
            serverSocket = new ServerSocket(port);
            game = new Game(this, playersList);

            ExecutorService pool = Executors.newFixedThreadPool(players);

            while (players <= 10) {
                Socket socket = serverSocket.accept();
                player = new Player(this, socket);
                playersList.add(player);
                pool.submit(player);

            }
            game.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void broadcast (Player player, String message){
        for (Player players: playersList) {
            if (player.equals(players)){
                continue;
            }

            DataOutputStream outMessage = null;
            try {
                outMessage = new DataOutputStream(players.getClientSocket().getOutputStream());
                outMessage.writeBytes(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void  broadcast(String message){
        for (Player players: playersList) {

            DataOutputStream outMessage = null;
            try {
                outMessage = new DataOutputStream(players.getClientSocket().getOutputStream());
                outMessage.writeBytes(message);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public synchronized void turnChange(){
        this.turn = true;
        broadcast(ACIIArt.yourTurn());
        notifyAll();
    }

    public Game getGame(){
        return game;
    }

    public CopyOnWriteArrayList<Player> getPlayersList() {
        return playersList;
    }

}

