import java.io.*;
import java.lang.Thread;
import java.net.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
public class ClientStart extends JFrame
{	
	JLabel l1,l2;
	JTextField t1,t2;
	JButton cnt;
	static ArrayList<ClientChatRoom> chatRoomList;
	
	boolean con=false;
	Socket socket=null;
	Server server;
	
	public ClientStart(String s)
	{
		super(s);
		server=new Server();
		chatRoomList=new ArrayList<ClientChatRoom>();
	}

	public void setView()
	{
		 l1=new JLabel("Enter your Name ");
		 l2=new JLabel("Enter IP address ");
		 t1=new JTextField();
		 t2=new JTextField();
		 cnt=new JButton("CONNECT");	
		 setLayout(null);

		 l1.setBounds(25,25,100,30);
		 l2.setBounds(25,80,100,30);
		 t1.setBounds(150,25,100,30);
		 t2.setBounds(150,80,100,30);
		 cnt.setBounds(50,150,100,50);

		 this.add(l1);
		 this.add(l2);
		 this.add(t1);
		 this.add(t2);
		 this.add(cnt);
		 
		 cnt.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent ae)
			 {
				 try{	              
						socket=new Socket(t2.getText().toString(),3001);	
						String username = t1.getText().toString();
						ClientChatRoom chatroom=new ClientChatRoom(socket,username,username.toUpperCase());
						chatroom.runIt();
						chatRoomList.add(chatroom);
						server.someOneJoined(socket,username);
						//dispose();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
			 }
		 });
		 
		 this.addWindowListener(new java.awt.event.WindowAdapter() {
		 @Override
		 public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        
			try{
				socket.close();
				System.out.println("closing");
				System.exit(0);
			}catch(Exception e){e.printStackTrace();}
        
    }
});
	
	}
		 public static void main(String []args)
		 {
			ClientStart cs=new ClientStart("CLIENT");
	 		cs.setSize(300,300);
			cs.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			cs.setView();
			cs.setVisible(true);
		 }
}

