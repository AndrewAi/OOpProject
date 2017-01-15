# OOP Project

3rd Year Objected orientated programming project.

##Project Brief:
You are required to implement a multi-threaded file server and logging application that allows
a client application to download files using a set of options presented in a terminal user
interface.

### Expected Functionality: 

The Appicatin should allow many users/clients to connect to server simultanously.
this is done by having the server multi thread.
When a client makes a new socket server connection request. the server will receive the incoming request and
pass it off to a new thread within the server. this will allow for mulitple simuloutanous client requests.

Once the application starts the client will be prompted with a menu:
  Enter 1. Connect to server
  Enter 2. List Files available for download.
  Enter 3. Download File
  Enter 4. Quit
  
 
The is not allowed to enter options 2 or 3 if they are not connected to the server.

## Code Design Approach:

Id most of the logic on the client side to keep it straight forward and simple as possible.
When the program starts the user is entered into a while loop where they are prompted with menu options.
the while loop will not finish until the user enters 4 to quit.
when a user enters an option 1,2,3,4 relevent method will be called and the method in turn will commuincate
witht the server. 
I have chosen that the server will only send back data(file names,files) to be then handled and displayed
by the client. Most dialogue will be displayed by the client.

