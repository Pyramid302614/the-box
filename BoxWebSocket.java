import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

public class BoxWebSocket {

    public static String boxEngineServer = "beetroot.pyramidstudios.xyz";
    private static WebSocket webSocket;

    public static void connect(String server) {

        System.out.println("Connecting to server: " + server);

        HttpClient client = HttpClient.newHttpClient();
        webSocket = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://" + server), new WebSocket.Listener() {

                    @Override
                    public void onOpen(WebSocket webSocket) {
                        System.out.println("Connected! :D");
                        System.out.println(" ============ [ WELCOME TO THE BOX! ] ============ ");
                        webSocket.sendText("join>"+Main.clientId,true);
                        WebSocket.Listener.super.onOpen(webSocket);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        String msg = data.toString();
                        switch(msg.split(">")[0]) {
                            case "join":
                                System.out.print("\r"+msg.substring("join>".length()) + " joined.\n");
                                break;
                            case "leave":
                                System.out.print("\r"+msg.substring("leave>".length()) + " left.\n");
                                break;
                            case "message":
                                String author = msg.split("&&&&&&&&")[1];
                                if(!author.equals(Interface.me))
                                    System.out.print("\r"+(msg.split("&&&&&&&&")[1]) + " >> " + (msg.substring("message>".length()).split("&&&&&&&&")[0])+"\n");
                                break;
                            case "name":
                                System.out.print("\r"+(msg.substring("name>".length()).split("&&&&&&&&")[0]) + " has changed their name to " + msg.split("&&&&&&&&")[1]);
                                break;
                            default:
                                System.out.print("\r[RAW] " + msg + "\n");
                                break;
                        }
                        return WebSocket.Listener.super.onText(webSocket, data, last);
                    }

                    @Override
                    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
                        System.out.println("[RAW] Received binary data: " + data);
                        return WebSocket.Listener.super.onBinary(webSocket, data, last);
                    }

                    @Override
                    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                        System.out.println("[RAW] Closed: " + statusCode + " " + reason);
                        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        System.err.println("[RAW] Error: " + error.getMessage());
                    }

                })
                .join();

    }

    public static void disconnect() {

        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Manual Disconnect").join();

    }

    public static void out(String message) {

        if(webSocket != null ) webSocket.sendText(message,true);

    }

}