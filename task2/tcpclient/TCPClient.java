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
        ByteArrayOutputStream dynamicToClient2 = new ByteArrayOutputStream();
        int serverStreamSize =  0;


        if (toServerBytes != null){
            mySocket.getOutputStream().write(toServerBytes);
            //System.out.println("this is sent " + toServerBytes.length);

        }

        if(this.shut){
            mySocket.shutdownOutput();
        }

        if(this.time != null){
            mySocket.setSoTimeout(this.time);
        }

        while( serverStreamSize != -1){
            try {
                serverStreamSize = mySocket.getInputStream().read(fromServerBytes);
                //System.out.println("stream size " + serverStreamSize);
                dynamicToClient.write(fromServerBytes, 0, serverStreamSize);
            } catch (Exception e) {
                serverStreamSize = -1;

            }
        }

        if(this.lim != null && this.lim < toServerBytes.length){
            
            dynamicToClient2.write(dynamicToClient.toByteArray(), 0, this.lim);
        }


        mySocket.close();
        //System.out.println("this is printed " + dynamicToClient.size());

        if(this.lim == null||this.lim > toServerBytes.length){return dynamicToClient.toByteArray();}
        else{return dynamicToClient2.toByteArray();}
    }
}
