//Created By Alan
//2019-1-27
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import common.MessageInfo;

public class UDPClient {
	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		// TO-DO: Construct UDP client class and try to send messages
		UDPClientInst client = new UDPClientInst();
		client.testLoop(serverAddr,recvPort,countTo);
	}
}

	class UDPClientInst {
		// TO-DO: Initialise the UDP socket for sending data
		private DatagramSocket SENDSOC;
		public UDPClientInst(){
			try{
				SENDSOC = new DatagramSocket();
			}catch(SocketException e){
				System.out.println("Socket: " + e.getMessage());
			}
		}

		public void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
			// TO-DO: Send the messages to the server
			for (int	tries = 0; tries<countTo ;tries++){
				MessageInfo msg = new MessageInfo(countTo,tries);
				send(msg.toString(),serverAddr,recvPort);
			}
		}
		public void send(String payload, InetAddress destAddr, int destPort) {
			int				payloadSize;
			byte[]				pktData;
			DatagramPacket		pkt;
			// TO-DO: build the datagram packet and send it to the server
			try{
				pktData = payload.getBytes(StandardCharsets.US_ASCII);
				payloadSize = pktData.length;
				pkt = new DatagramPacket(pktData,payloadSize,destAddr,destPort);
				String str = new String(pkt.getData(), StandardCharsets.US_ASCII);
				System.out.printf("%s", str);
				SENDSOC.send(pkt);
			}catch(IOException e){
				System.out.println("IO: " + e.getMessage());
			}
		}
	}
