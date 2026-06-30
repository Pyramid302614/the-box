import java.util.Scanner;

public class Interface {

    private static Scanner sc;
    private static boolean interfaceRunning = false;

    public static String me = "????";

    public static void start() {

        sc = new Scanner(System.in);
        interfaceRunning = true;

        while(interfaceRunning) {
//            System.out.print(me + " >> ");
            String input = sc.nextLine();
            if(input.equals("/exit")) end();
            else if(input.matches("/name [^ ]+")) {
                me = input.substring("/name ".length());
                BoxWebSocket.out("name>"+me);
            }
            else if(input.matches("/server [^ ]+")) {
                String previousServer = Main.server;
                Main.server = input.substring("/server ".length()).replace("default",BoxWebSocket.boxEngineServer);
                BoxWebSocket.disconnect();
                try {
                    BoxWebSocket.connect(Main.server);
                } catch(Exception e) {
                    System.err.println("Oops! I couldn't quite connect to that server:\n"+e);
                    System.out.println("Switch back to " + previousServer + "? [Y/N]");
                    if(sc.nextLine().equalsIgnoreCase("y")) {
                        Main.server = previousServer;
                        BoxWebSocket.connect(Main.server);
                    } else end();
                }
            }
            else sendMessage(new Message(input,me));
        }

    }
    public static void end() {

        interfaceRunning = false;
        System.out.println("[Interface End]");
        try {
            sc.close();
            BoxWebSocket.disconnect();
        } catch(Exception ignored) {}

    }

    public static void sendMessage(Message message) {

        BoxWebSocket.out("message>"+message.content+"&&&&&&&&"+message.author);

    }

}
