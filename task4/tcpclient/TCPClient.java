package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    byte[] fromServerBytes = new byte[1024];
    boolean shut;
    Integer time;
    Integer lim;
    
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shut = shutdown;
        this.time = timeout;
        this.lim = limit;

    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {

        Socket mySocket = new Socket(hostname, port);
        ByteArrayOutputStream dynamicToClient = new ByteArrayOutputStream();
        int serverStreamSize =  0;


        if (toServerBytes != null){
            mySocket.getOutputStream().write(toServerBytes);
        }

        if(this.shut){
            mySocket.shutdownOutput();
        }

        if(this.time != null){
            mySocket.setSoTimeout(this.time);
        }

        if(this.lim == null){
            while( serverStreamSize != -1){
                try {
                    serverStreamSize = mySocket.getInputStream().read(fromServerBytes);
                    dynamicToClient.write(fromServerBytes, 0, serverStreamSize);
    
                } catch (Exception e) {
                    serverStreamSize = -1;
    
                }
            }
        }

        else{
            while( serverStreamSize != -1){
                try {
                    serverStreamSize = mySocket.getInputStream().read(fromServerBytes);
                    if(serverStreamSize > this.lim ){
                        dynamicToClient.write(fromServerBytes, 0, this.lim);
                        serverStreamSize = -1;
                        break;
                    }
                    dynamicToClient.write(fromServerBytes, 0, serverStreamSize);
                } catch (Exception e) {
                    serverStreamSize = -1;
                }
            }
        }

        mySocket.close();
        return dynamicToClient.toByteArray();
    }
}
