public class Message {

    public String content;
    public String author;

    public Message(String content, String author) {

        this.content = content;
        this.author = author;

    }
    public String toSignal() {

        return content + "&&&&&&&&" + author;

    }

    public static Message fromSignal(String signal) {

        final String[] split = signal.split("&&&&&&&&");
        return new Message(
                split[0],
                split[1]
                );

    }

}
