import java.net.*;
import java.io.*;
import tcpclient.TCPClient;


public class MyRunnable implements Runnable {

    ServerSocket welSo;

    public MyRunnable(ServerSocket welcomeSocket){
        this.welSo = welcomeSocket;

    }


    @Override
    public void run(){

        int buffSize = 1024;
        String ok = "HTTP/1.1 200 OK\r\n\r\n";
        String bad = "HTTP/1.1 400 Bad Request\r\n\r\n";
        String notFound = "HTTP/1.1 404 Not Found\r\n\r\n";


        byte[] fromServerBytes = new byte[buffSize];
                ByteArrayOutputStream dynamicToClient = new ByteArrayOutputStream();
                boolean ask = false; // Check ask
                boolean get = false; //check get
                boolean http = false; //check http
                String host = null; //Host to send data to 
                Boolean hostReq = false; //checking if Host: is included 
                int hPort = 0; // the host port
                byte[] text = null; // optional data to be sent to host
                boolean shut = false; // opional shut down
                Integer time = null; // optional timeout
                Integer lim = null; // optional data limit
                String txt =  null;


                try {          
                Socket connectionSo = welSo.accept();
                int serverStreamSize =  buffSize;
                while( serverStreamSize >= buffSize){
                        serverStreamSize = connectionSo.getInputStream().read(fromServerBytes);
                        dynamicToClient.write(fromServerBytes, 0, serverStreamSize);
                }


                String uri[] = dynamicToClient.toString().split("\\r\\n")[0].split("[?=& ]");
                String uri2[] = dynamicToClient.toString().split("\\r\\n")[1].split("[?=& ]");

                for(int i = 0; i < uri2.length; i++){
                    if(uri2[i].contains("Host:")){
                        hostReq = true;
                    }
                }
                
                for(int i = 0; i < uri.length; i++){
                    if(uri[i].equals("GET")){
                        get = true;
                    }
                    else if(uri[i].equals("/ask")){
                        ask = true;
                    }
                    else if(uri[i].contains("HTTP/1.1")){
                        http = true;
                    }
                    else if(uri[i].equals("shutdown")){
                        shut = Boolean.parseBoolean(uri[++i]);
                    }
                    else if(uri[i].equals("limit")){
                        lim = Integer.parseInt(uri[++i]);
                    }
                    else if(uri[i].equals("timeout")){
                        time = Integer.parseInt(uri[++i]);
                    }
                    else if(uri[i].equals("hostname")){
                        host = uri[++i];
                    }
                    else if(uri[i].equals("port")){
                        hPort = Integer.parseInt(uri[++i]);
                    }
                    else if(uri[i].equals("string")){
                        text = uri[++i].getBytes();
                        txt = new String(text);
                    }
                }
                System.out.println("this is ask: " + ask);
                System.out.println("this is get: " + get);
                System.out.println("this is shutdown: " + shut);
                System.out.println("this is limit: " + lim);
                System.out.println("this is timeout: " + time);
                System.out.println("this is host: " + host );
                System.out.println("this is port: " + hPort );
                System.out.println("this is string: " + txt);


                if (hostReq && http && get && host != null && hPort != 0) {
                    if (!ask) {
                        connectionSo.getOutputStream().write(notFound.getBytes());
                        connectionSo.getOutputStream().write("404 Not Found".getBytes());
                    }
                    else{
                        try {
                            TCPClient tcpClient = new tcpclient.TCPClient(shut, time, lim);
                            String serverAnswer = new String(tcpClient.askServer(host, hPort, text));
                            
                            connectionSo.getOutputStream().write(ok.getBytes());
                            connectionSo.getOutputStream().write(serverAnswer.getBytes());

                        } catch (IOException e) {
                            connectionSo.getOutputStream().write(notFound.getBytes());
                            connectionSo.getOutputStream().write("404 Not Found".getBytes());
                        }
                    }
                }
                
                else {
                    connectionSo.getOutputStream().write(bad.getBytes());
                    connectionSo.getOutputStream().write("400 Bad Request".getBytes());
                }
                connectionSo.getOutputStream().flush();
                connectionSo.close();

                } catch(IOException ex) {
                    System.err.println(ex);
                    System.exit(1);
                }


    }

}

