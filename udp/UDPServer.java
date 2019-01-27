/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int countdown;
	private boolean[] receivedMessages;
	private boolean close;
	private int count=0;
	private static final int buffersize = 1000;
	private static final int timeout = 20000;
	private static final int MAX = 3;
//static final => CONSTANTS in C++

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;
		byte[]  buffer = new byte[buffersize];
		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		while(!close&&count<MAX){
			try{
				pac = new DatagramPacket(buffer,buffersize);
				recvSoc.receive(pac);
				String data = new String(pac.getData(),StandardCharsets.US_ASCII);
				processMessage(data);
				count=0;
			}catch(Exception e){
				System.out.println("Exception: " + e.getMessage());
				count++;
			}
		}
	}

	public void processMessage(String data) {

		MessageInfo msg = null;
		try{
			// TO-DO: Use the data to construct a new MessageInfo object
			msg = new MessageInfo(data);
			// TO-DO: On receipt of first message, initialise the receive buffer
			if (totalMessages==-1){
				totalMessages=msg.totalMessages;
				receivedMessages= new boolean[totalMessages];
				countdown=totalMessages;
			}
			// TO-DO: Log receipt of the message
			System.out.printf("%d,", msg.messageNum);
			receivedMessages[msg.messageNum] = true;
			countdown--;
			// TO-DO: If this is the last expected message, then identify
			//        any missing messages
			if (countdown==0){
				close = true;
			}
		}catch(Exception e){
			System.err.println("Ignored: " + e.getMessage());
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
			recvSoc.setSoTimeout(timeout);
		}catch(SocketException e){
			System.out.println("SocketException: " + e.getMessage());
		}
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;
		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);
		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

}
