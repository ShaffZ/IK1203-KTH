import java.net.*;
import java.io.*;



/**
 * @author : Shafeek Zakko
 * This code is from my project submission from last year IK1203 V22
 */

public class ConcHTTPAsk {

    private static void usage() {
        System.err.println("Usage: HTTPAsk port");
        System.exit(1);
    }


    public static void main( String[] args) {

        int port = 0; 

        try {port = Integer.parseInt(args[0]);}
        catch (Exception ex) {usage();}

        try {
            ServerSocket welcomeSo = new ServerSocket(port);

            while(true){
                MyRunnable thrd = new MyRunnable(welcomeSo); 
                Thread client = new Thread(thrd);
                client.start();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
 
