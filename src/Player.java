import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.Socket;

public class Player implements Runnable {

    private Prompt prompt;
    private Socket clientSocket;
    private String name;
    private PrintStream out;
    private InputStream in;
    private GameServer gameServer;
    private ACIIArt art;

    Player(GameServer gameServer, Socket clientSocket) {
        this.gameServer = gameServer;
        this.clientSocket = clientSocket;
    }

    private void start(){

        try{
            this.out = new PrintStream(clientSocket.getOutputStream());
            this.in = clientSocket.getInputStream();
            this.prompt = new Prompt(in, out);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        start();
        this.name = setName();

        BufferedReader userInputStream = null;
        String message = "";

        while (!clientSocket.isClosed()){
            synchronized (this){
                try{
                    userInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    if ((message = userInputStream.readLine()) == null){
                        exit();
                        return;
                    }
                    System.out.println("Press r to roll the dice");
                    getPlayerInput(message);

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public int rollDice() {
        int dice = (int) (Math.ceil(Math.random() * 6));
        System.out.println("You rolled " + dice);
        if (dice == 1){
            System.out.println(art.dice1());
        }
        if (dice == 2){
            System.out.println(art.dice2());
        }
        if (dice == 3){
            System.out.println(art.dice3());
        }
        if (dice == 4){
            System.out.println(art.dice4());
        }
        if (dice == 5){
            System.out.println(art.dice5());
        }
        if (dice == 6){
            System.out.println(art.dice6());
        }
        return dice;
    }

    private String setName(){
        String user = Thread.currentThread().getName();
        return "Knight " + user.substring(user.length() -1);
    }

    private void getPlayerInput(String message){
        if (message.equals("")){
            return;
        }
        if (message.equals("r")){
            rollDice();
        }
    }

    public void broadcastMessage(String message){
        gameServer.broadcast(this, message);
    }

    private void exit(){

        try {
            in.close();
            out.close();
            clientSocket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public Prompt getPrompt(){
        return prompt;
    }
}
