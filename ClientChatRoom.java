import java.io.*;
import java.lang.Thread;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ClientChatRoom extends JFrame
{
	JTextArea ta;
	JButton send;
	// JLabel name;
	Socket socket;
	String myname;
	DataInputStream dis;
	DataOutputStream dos;
	long ta_length;
	public ClientChatRoom() //empty constructor
	{
		
	}
	public ClientChatRoom(Socket socket,String username,String name) //constructor
	{
		super(name);
		this.socket=socket;
		this.myname=username;
		try{
			dos=new DataOutputStream(socket.getOutputStream());
			dis=new DataInputStream(socket.getInputStream());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String getUserName()
	{
		return this.myname;
	}

	public void newEntry(Socket s,String username)
	{
		if(s!=socket)
		{
			ta.append("\n<"+username+"> has joined the chatroom");
		}
		else
		{
			ta.append("\n You have joined the chatroom as "+username);
			
			if(ClientStart.chatRoomList.size()!=1) //only one chatroom the don't print 'with'
				ta.append("\nWith");
				
			for(ClientChatRoom cr : ClientStart.chatRoomList)
			{
				if( !cr.getUserName().equals(myname) ) //only print other user's name 
					ta.append("\n"+cr.getUserName());
			}
		}
		ta.append("\n<"+myname+">:");
		ta_length=ta.getText().length();
	}
	public void setView()
	{
		setLayout(null);
		
		/*
		name=new JLabel(myname);
		name.setBounds(120,0,300,50);
		this.add(name);
		*/	
		ta=new JTextArea();
		//try{ta.setLayout(new BoxLayout(ta,BoxLayout.Y_AXIS));}catch(Exception e){e.printStackTrace();}
		//ta.setBounds(0,60,300,300);
		//this.add(ta);
		
		JScrollPane scroll = new JScrollPane (ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(0,60,300,300);
		this.add(scroll);
		
		send=new JButton("SEND");
		send.setBounds(150,380,100,50);
		this.add(send);
		
		ta.append("Connected to chatroom");
		
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				String line;
				line=(ta.getText()).substring((int)ta_length,ta.getText().length());
				sendMessage(line);
				
				//updating length variable
				ta_length=ta.getText().length();
			}
		});
	
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		 @Override
		 public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        
			try{
				new Server().someOneLeft(myname);
				System.out.println(myname + "left the chat");
			}catch(Exception e){e.printStackTrace();}
		 }
	});
	}
	
	public void left(String name)
	{
		if(!name.equals(myname))
		{
			ta.append("\n<"+name+"> has left the chatroom");
			ta.append("\n<"+myname+">:");
			ta_length=ta.getText().length();
		}
	}
	public void sendMessage(String line)
	{
		try{
			if(dos == null)
				System.out.println("dos is null");
		dos.writeUTF("<"+myname+">: "+line);
		dos.flush();
		}catch(Exception e){e.printStackTrace();}
	}
	
	Thread messageThread=new Thread(new Runnable(){
		public void run()
		{
			try{
			while(true)
			{
				String line=dis.readUTF();
				//System.out.println(myname + "after readUTF()");
				if(!line.contains("<"+myname+">"))
				{
					ta.append(line);
				}
				ta.append("\n<"+myname+">: ");
				ta_length=ta.getText().length();
			}
			}catch(Exception e){e.printStackTrace();}
		}
	});
	
	/*
	public void incomingMessage(String line)
	{
		ta_length=ta.getText().length();
	}
	*/
	
	public void runIt()
	{
		setSize(500,500);
		setView();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		messageThread.start();
	}
}