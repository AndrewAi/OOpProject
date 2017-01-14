/**
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
    String message = "";
    String userInput;
    String ipaddress = "192.168.1.101";
    Scanner stdin;
    boolean serverConnected = false;

    Client() {
    }

    void run() throws IOException {

        // IP ADDRESS
        //   192.168.1.103
        //  192.168.1.104
        // 192.168.1.102

        // program opens
        // menu prompts, and asks user enter 1 to ConnectServer, 2: List Files, 3: Download file, 4: Disconnect/Quit



        menu();

            //3: Communicating with the server

            // do while loop, while user input (message) is not equal to exit


        //} catch (UnknownHostException unknownHost) {
          //  System.err.println("You are trying to connect to an unknown host!");
        //} catch (IOException ioException) {
          //  ioException.printStackTrace();
       // }
        // after while loop has finished close connection to server

    }


    public void menu() throws IOException {
        do{
        stdin = new Scanner(System.in);
        //try {
        //1. creating a socket to connect to the server
        System.out.println("1: Connect to Server ");
        System.out.println("2: List Files ");
        System.out.println("3: Download File ");
        System.out.println("4: Quit ");



        userInput = stdin.next();

        if (userInput.equalsIgnoreCase("1")) {

            if (serverConnected){
                System.out.println("Server Already connected");

            } else {

                try {

                    // create a socket bewtween client and ip address of the server on port 2004
                    requestSocket = new Socket(ipaddress, 7777);
                    System.out.println("Connected to " + ipaddress + " in port 7777");
                    //2. get Input and Output streams
                    out = new ObjectOutputStream(requestSocket.getOutputStream());
                    out.flush();
                    in = new ObjectInputStream(requestSocket.getInputStream());
                    serverConnected = true;

                    //serverConnectedMenu();


                } catch (IOException io) {
                    io.printStackTrace();
                }
            }



        }

        else if (userInput.equalsIgnoreCase("2")){

            if (!serverConnected){
                System.out.println("Connect to server first, before you can list files");
                menu();
            }


            System.out.println("Client Option 2 Selected, Listing Files");

            sendMessage(userInput);



            try {


                //ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());
                //ArrayList<String> clientFileList = (ArrayList<String>) in.readObject(); //Deserialise
                Object object = in.readObject();
                ArrayList<String> clientFileList = (ArrayList<String>) object;

                System.out.println("ClientFileList: " + clientFileList.toString());
            }
            catch (IOException e){
                e.printStackTrace();
                System.out.println("FAiled");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Class Not found option 2 io");
            }


            //serverConnectedMenu();
        }



        else if (userInput.equalsIgnoreCase("3")){

            if (!serverConnected){
                System.out.println("Connect to server first, before you can download files");
                menu();
            }

            System.out.println("Client Option 3 Selected.");

            sendMessage(userInput);

           /* FileOutputStream fIn = new FileOutputStream("testing");
            BufferedInputStream inAout = new BufferedInputStream(requestSocket.getInputStream());
            byte fileContent[] = new byte[1000000];
            inAout.read(fileContent);
            fIn.write(fileContent);
            System.out.println("file Downloaded");
            System.out.println("fileContent:" + fileContent);
            */

          /*  byte[] mybytearray = new byte[1024];
            //InputStream is = sock.getInputStream();
            FileOutputStream fos = new FileOutputStream("testing2.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int bytesRead = in.read(mybytearray, 0, mybytearray.length);
            bos.write(mybytearray, 0, bytesRead);
            System.out.println("file transfer complete");
            */

            try {


                BufferedInputStream bis = new BufferedInputStream(requestSocket.getInputStream());
                DataInputStream dis = new DataInputStream(bis);

                long fileLength = dis.readLong();
                String fileName = dis.readUTF();

                File file = new File("testing3.txt");

                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for (int j = 0; j < fileLength; j++)
                    bos.write(bis.read());

                bos.close();
                dis.close();

                System.out.println("file transfer complete");
            }
            catch (IOException io){
                io.printStackTrace();
            }


        }




        else if (userInput.equalsIgnoreCase("4")){
            System.out.println("else if option 4 selected");
            closeConnection();
        }




        }while (!userInput.equalsIgnoreCase("4"));

    }





    public void serverConnectedMenu() {

        do {
            try {
                // read in message sent from server and display it to the user

                // // TODO: 14/01/2017 if user input from main menu prompt is connectServer parse Xml file here possibly


                try {
                    message = (String) in.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(message);

                // send user input to the server to be acted on.
                message = stdin.next();
                sendMessage(message);


            } catch (ClassNotFoundException classNot) {
                System.err.println("data received in unknown format");
            }
        } while (!message.equals("4"));
        System.out.println("Client Option 4 Selected, While End");
        //closeConnection();
    }



    public void closeConnection(){

        System.out.println("method Client Option 4 Selected");
        //4: Closing connection
        try {
            in.close();
            out.close();
            requestSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
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

    // when the main method is called, create a new instance of the Client class.
    // and call its run method. which  is located above,
    public static void main(String args[]) throws IOException {
        Client client = new Client();
        client.run();
    }
}
