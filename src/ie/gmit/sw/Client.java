package ie.gmit.sw; /**
 * Created by AndrewIrwin on 01/01/2017.
 */


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    BufferedInputStream bis;
    DataInputStream dis;
    FileOutputStream fos;
    BufferedOutputStream bos;


    String message = "";
    String userInput;
    String ipaddress;
    Scanner stdin;
    boolean serverConnected = false;

    Client() {
    }

    void run() throws Exception {



        // program opens
        // menu prompts, and asks user enter 1 to ConnectServer, 2: List Files, 3: Download file, 4: Disconnect/Quit

        menu();


    }


    public void menu() throws Exception {
        do {
            stdin = new Scanner(System.in);

            //prompting user for input
            System.out.println("1: Connect to ie.gmit.sw.Server ");
            System.out.println("2: List Files ");
            System.out.println("3: Download File ");
            System.out.println("4: Quit ");
            System.out.println("\nType Option [1-4]");


            userInput = stdin.next();

            if (userInput.equalsIgnoreCase("1")) {

                if (serverConnected) {
                    System.out.println("ie.gmit.sw.Server Already connected");

                } else {

                    //1. creating a socket to connect to the server
                    connectToServer(false);

                }


            } else if (userInput.equalsIgnoreCase("2")) {

                if (!serverConnected) {
                    System.out.println("Connect to server first, before you can list files");
                    menu();
                }


                System.out.println("Option 2 Selected, Listing Files");


                sendMessage(userInput);

                readFileList();


            } else if (userInput.equalsIgnoreCase("3")) {

                if (!serverConnected) {
                    System.out.println("Connect to server first, before you can download files");
                    menu();
                }

                System.out.println("Option 3 Selected.");


                sendMessage(userInput);

                String serverMessage = (String) in.readObject();
                System.out.println(serverMessage);
                userInput = stdin.next();


                sendMessage(userInput);

                readDownloadFile();
                closeConnection();
                connectToServer(true);


            } else if (userInput.equalsIgnoreCase("4")) {
                closeConnection();
            }


        } while (!userInput.equalsIgnoreCase("4"));

    }






    public void connectToServer(boolean reConnection) throws Exception {



        if (!reConnection) {
            try {

                System.out.println("Please Enter Ip Address: ");
                ipaddress = stdin.next();
            } catch (Exception e) {
               e.printStackTrace();
                System.out.println("Invalid Ip Address");
                menu();
            }
        }




        try {

            // create a socket bewtween client and ip address of the server on port 2004
            requestSocket = new Socket(ipaddress, 7777);
            if (!reConnection)
                System.out.println("Connected to " + ipaddress + " in port 7777");
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            serverConnected = true;




        } catch (IOException io) {
            io.printStackTrace();
        }
    }




    public void closeConnection() {

        System.out.println("Option 4 Selected, Quiting");
        //4: Closing connection
        try {


            in.close();
            out.close();
            requestSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }





    public void readFileList() {




        try {


            Object object = in.readObject();
            ArrayList<String> clientFileList = (ArrayList<String>) object;

            System.out.println("ClientFileList: " + clientFileList.toString());//Deserialise
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error Reading Files");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Option 2 Error");
        }
    }


    public void readDownloadFile() {


        try {


            bis = new BufferedInputStream(requestSocket.getInputStream());
            dis = new DataInputStream(bis);

            long fileLength = dis.readLong();
            String fileName = dis.readUTF();

            // used for testing file download.
            fileName += 1;

            System.out.println("fileName: " + fileName);

            File file = new File(fileName);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++)
                bos.write(bis.read());


            bos.close();
            dis.close();
            System.out.println("File download complete");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    // method used to send message to server
    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            // System.out.println("client>>>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // when the main method is called, create a new instance of the ie.gmit.sw.Client class.
    // and call its run method. which  is located above,
    public static void main(String args[]) throws Exception {
        System.out.println("ie.gmit.sw.Client Started");

        Client client = new Client();
        client.run();
    }
}
