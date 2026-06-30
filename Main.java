import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static String clientId;
    public static String server;

    public static void main(String[] args) {

        server = args.length > 0 ? args[0] : BoxWebSocket.boxEngineServer;

        try {
            System.out.println("Fetching pre-existing client ID...");
            File clientIdFile = new File("ClientID");
            if(clientIdFile.exists()) {
                Scanner sc = new Scanner(clientIdFile);
                clientId = sc.nextLine();
                sc.close();
            } else {
                clientIdFile.createNewFile();
                FileWriter fw = new FileWriter(clientIdFile);
                clientId = ((int)(Math.floor(Math.random()*1_000_000_000))) + "";
                fw.write(clientId);
                fw.close();
            }
        } catch(Exception ignored) {}

        System.out.println(" ================= [ STARTING ] ================= ");

        System.out.println("Finding out who you are... [ Protocol: HTTP - Url: /me ]");
        try {
            Interface.me = BoxServer.me();
            System.out.println("You are " + Interface.me + "!");
        } catch(Exception e) {
            System.err.println("Unable to fetch your name: " + e.getMessage());
        }

        try {

            BoxWebSocket.connect(server);
            Runnable heartbeat = () -> BoxWebSocket.out("heartbeat");
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(heartbeat,20,20, TimeUnit.SECONDS);

            try {

                Interface.start();

            } catch(Exception e) {
                System.out.println("Interface crashed. :(\nWould you like the full error? [Y/N]");
                Scanner sc = new Scanner(System.in);
                if(sc.nextLine().equalsIgnoreCase("y")) e.printStackTrace();
                sc.close();
            }
        } catch(Exception e) {
            System.err.println("Failed to connect to server. :(\nWould you like the full error? [Y/N]");
            Scanner sc = new Scanner(System.in);
            if(sc.nextLine().equalsIgnoreCase("y")) e.printStackTrace();
            sc.close();
        }

    }

}
