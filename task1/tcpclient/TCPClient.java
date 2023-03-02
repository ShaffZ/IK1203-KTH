package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    byte[] fromServerBytes = new byte[1024];

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {


        Socket mySocket = new Socket(hostname, port);
        mySocket.setSoTimeout(5000);

        ByteArrayOutputStream dynamicToClient = new ByteArrayOutputStream();




        if (toServerBytes != null){
            mySocket.getOutputStream().write(toServerBytes);
            mySocket.shutdownOutput();
        }

        int serverStreamSize =  0;

        while( serverStreamSize != -1){
            try {
                serverStreamSize = mySocket.getInputStream().read(fromServerBytes);
                dynamicToClient.write(fromServerBytes, 0, serverStreamSize);

            } catch (Exception e) {
                serverStreamSize = -1;

            }
        }


        mySocket.close();
        return dynamicToClient.toByteArray();
    }
}
