import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost",23354));

        while(true) {
            try(SocketChannel socketChannel = serverChannel.accept()) {

                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                final List<String> words = new ArrayList<>();

                while(socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if(bytesCount == -1)break;

                    final String msg = new String(inputBuffer.array(), 0,bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    String[] arr = msg.split(" ");
                    String str = String.join("", arr);
                    System.out.println("Получено сообщение от клиента: " + msg);
                    socketChannel.write(ByteBuffer.wrap(("Ваше сообщение без пробелов: " + str).getBytes(StandardCharsets.UTF_8)));
                }

            }catch (IOException err){
                System.out.println(err.getMessage());
            }
        }
    }
}
