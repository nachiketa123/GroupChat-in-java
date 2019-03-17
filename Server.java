import java.util.ArrayList;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.Thread;
import java.util.ArrayList;

public class Server
{
	static ArrayList <DataOutputStream> clients;
	ServerSocket ss=null;
	Socket s=null;
	DataInputStream dis;
	DataOutputStream dos;
	ClientChatRoom chatroom;
	public Server()
	{
		
	}

	public void someOneJoined(Socket s,String username)
	{		
		for(ClientChatRoom cr :ClientStart.chatRoomList)
			{
				cr.newEntry(s,username);
			}
	}
	
	public void someOneLeft(String name)
	{
		for(ClientChatRoom cr :ClientStart.chatRoomList)
			{
				cr.left(name);
			}
	}
	
	public void createClient()
	{
			try{
				while((s=ss.accept())!=null)
				{
					try{
							dis=new DataInputStream(s.getInputStream());
							dos=new DataOutputStream(s.getOutputStream());
							//chatroom=new ClientChatRoom();
							//newEntry(username);
							Server.clients.add(dos);
							Thread t=new ClientHandler(s,dis,dos);
							t.start();
						}catch(Exception e)
						{
							e.printStackTrace();
						}
				}
			}catch(Exception e){e.printStackTrace();}
	}
	
	public static void main(String args[])
	{
		try{
		Server obj=new Server();
		obj.ss=new ServerSocket(3001);
		System.out.println("Server Started");
		Server.clients =new ArrayList<DataOutputStream>();
		obj.createClient();
		obj.ss.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
class ClientHandler extends Thread
{
	static int COUNT;
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	public ClientHandler(Socket s,DataInputStream dis,DataOutputStream dos)
	{
		this.s=s;
		this.dis=dis;
		this.dos=dos;
		COUNT++;
	}
	public void run()
	{
		try{
			System.out.println(ClientHandler.COUNT + " Client Connected");
			//System.out.println("Waiting");
			Scanner in=new Scanner(System.in);
			while(true)
				{
					String msg=dis.readUTF();
					sendToAll(Server.clients,msg);
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	public void sendToAll(ArrayList<DataOutputStream> clients,String msg) 
	{
		for(DataOutputStream dos : clients)
		{
			try{
				dos.writeUTF("\n"+msg);
				//System.out.println(msg);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}