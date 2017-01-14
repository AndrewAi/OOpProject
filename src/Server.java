/**
 * Created by AndrewIrwin on 01/01/2017.
 */

import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class Server {
    public static void main(String[] args) throws Exception {
        // Create a new socket connection
        ServerSocket m_ServerSocket = new ServerSocket(7777, 10);
        int id = 0;
        // while serverSocket is accepting
        while (true) {
            // accept incoming client connections
            Socket clientSocket = m_ServerSocket.accept();

            //take the incoming client connection and pass it onto a new thread to be handled.
            // in this way may client connections can be handled at once, making the server multi threaded.
            ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
            // start each subsquent thread. which will lead below to the public void run method. where the thead will run.
            cliThread.start();
        }
    }
}

// class that contains information on each socket connection within each thread.
class ClientServiceThread extends Thread {

    Socket clientSocket;
    String clientResponse;
    int clientID = -1;
    ObjectOutputStream out;
    ObjectInputStream in;
    Boolean clientConnected = true;

    // constructor gets called above in EchoServer inner class and is passed incoming client socket connection information.
    // as well as client id.
    // as each client request comes through it is put into a new thread and passed through to this construcotr.
    ClientServiceThread(Socket s, int i) {
        clientSocket = s;
        clientID = i;
    }





    // method used to send message to the client
    void sendMessage(String msg) {
        try {
            // send an object to the client
            out.writeObject(msg);
            // clear the buffer to make sure nothing is left over in it.
            out.flush();
            System.out.println("client>> " + msg);

            // if there was a input/output problem catch it.
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // run the thread, if there are multiple connections/ threads this is where they will be run.
    public void run() {


        // create an object stream to the connected client. this is basically a bridge used to send messages to the
        // client.
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();

            // create an input stream with connected client to accept incoming commuincations/messages from client
            in = new ObjectInputStream(clientSocket.getInputStream());

            // print out information of the accpeted client connection
            System.out.println("Accepted Client : ID - " + clientID + " : Address - "
                    + clientSocket.getInetAddress().getHostName());


            // do below while loop while userInput is not equal to exit. keep the program running.
            do {
                try {


                    //if (clientConnected)
                    mainMenu();


                } catch (ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }

                //if (clientResponse.equalsIgnoreCase("4"))
                    //clientConnected = false;

            } while (!clientResponse.equalsIgnoreCase("4"));


            System.out.println("Ending Client : ID - " + clientID + " : Address - "
                    + clientSocket.getInetAddress().getHostName());




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void mainMenu() throws IOException, ClassNotFoundException {




        //("1Welcome, Enter 2 to List Files, 3 to Download a file. 4 to Quit.");
        clientResponse = (String) in.readObject();


        if (clientResponse.contains("2")) {

            System.out.println("Server Option 2");
            out.writeObject(listAllFiles());




        } else if (clientResponse.contains("3")) {

            System.out.println("Server Option 3");
            //sendMessage(listAllFiles());

            File file = new File("test.txt");


            /*FileInputStream fIn = new FileInputStream(file);
            ObjectOutputStream out2 = new ObjectOutputStream(clientSocket.getOutputStream());
            byte fileContent[] = new byte[(int) file.length()];
            fIn.read(fileContent);
            for (byte b : fileContent) {
                out2.write(b);
            }
            */

            byte[] mybytearray = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(mybytearray, 0, mybytearray.length);

            out.write(mybytearray, 0, mybytearray.length);
            out.flush();


        }
        else if (clientResponse.contains("4")){
            // disconnect
            System.out.println("Server Option 4");
            sendMessage("Server Option 2 selected");
            closeClientConnection();
        }


    }


    public static ArrayList listAllFiles() {


        File curDir = new File(".");
        File[] filesList = curDir.listFiles();
        ArrayList<String> fileNames = new ArrayList<String>();
        for(File f : filesList){

            if(f.isFile()){
                System.out.println("FileName: " + f.getName());


                fileNames.add(f.getName());
                System.out.println("Filenames: " + fileNames);
            }
        }






        /*File curDir = new File(".");
        File[] filesList = curDir.listFiles();
        ArrayList<String> fileNames = new ArrayList<String>();
        for(File f : filesList){
            //if(f.isDirectory())
            //  System.out.println("DirectoryName: " + f.getName());this is kamakazie stuff
            if(f.isFile()){
                System.out.println("FileName: " + f.getName());


                fileNames.add(f.getName());
                System.out.println("Filenames: " + fileNames);
            }
        }


       */
        return  fileNames;
    }



    public void downloadFile() throws IOException {



    }




    public void closeClientConnection(){
        try {
            out.close();
            in.close();
            clientSocket.close();
            System.out.println("Closed Connection Client : ID - " + clientID + " : Address - "
                    + clientSocket.getInetAddress().getHostName());
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }


}
