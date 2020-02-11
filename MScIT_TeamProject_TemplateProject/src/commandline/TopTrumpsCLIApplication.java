package commandline;

import java.io.*;
import java.util.*;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {


	static Map<Integer,String[]> arrs=new HashMap<Integer,String[]>();
	static String attrs[];
	static int num = 0;
	static int aiplayers = 4;
	static int allplayer = aiplayers+1;
	static List indexes = new ArrayList();
	static ArrayList[] cards = new ArrayList[allplayer];
	static boolean log = false;
	static FileOutputStream fos = null;
	static int pernum;
	static Map <Integer, String> players=new HashMap<Integer, String>();
	static int isai=0;
	static int end = 0;
	static List commonCards = new ArrayList();

	/**
	 * This main method is called by TopTrumps.java when the user specifies that they want to run in
	 * command line mode. The contents of args[0] is whether we should write game logs to a file.
	 */

	public static void txt2String(File file) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			int i = 0;
			while ((s = br.readLine()) != null) {
				if (i == 0) {
					attrs = s.split("	");
				} else {
					String[] tmp = s.split("	");

					arrs.put(i - 1, tmp);

				}
				i++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void getCards() throws Exception{
		File file = new File("StarCitizenDeck.txt");
		txt2String(file);
		return;
	}

	public static void menu(){
		System.out.println("Do you want to see past results or play a game?");
		System.out.println("   1: Print Game Statistics");
		System.out.println("   2: Play game");
	}
	public static void print(String str){
		System.out.println(str);
	}

	public static void shuffle() throws IOException {

		for(int a=0;a<num;a++){
			indexes.add(a);
		}

		for (int i = 0; i < allplayer; i++) {
			cards[i] = new ArrayList<Integer>();
		}
		Collections.shuffle(indexes);
		Collections.shuffle(indexes);

		if(log){
			fos.write("Shuffled cards\r\n".getBytes());
			StringBuffer aBuffer = new StringBuffer("");
			for(int i =0; i<indexes.size();i++){
				aBuffer.append(arrs.get(indexes.get(i))[0]+" ");
				if(i>0 && i%4==0){
					aBuffer.append("\r\n");
				}
			}
			byte[] bytesArray = aBuffer.toString().getBytes();
			fos.write(bytesArray);
			fos.write("\r\n----------\r\n".getBytes());
		}
		for(int i=0;i<allplayer;i++){
			for(int j=0;j<pernum;j++){
				int a;
				try{
					a = (Integer) indexes.get(1);
				}catch(Exception e){
					a = (Integer) indexes.get(0);
				}
				cards[i].add(a);
				//System.out.println(a);
				try{
					indexes.remove(1);
				}catch(Exception e){
					indexes.remove(0);
				}
			}
		}
	}

	public static int showChoose(Scanner sc, int nowround){
		int choose = 1;
		if(nowround==0){
			print("It is your turn to select a category, the categories are:");
			for(int i=1;i<attrs.length;i++){
				print(" "+i+": "+attrs[i]);
			}
			System.out.print("Enter the number for your attribute: ");
			choose = sc.nextInt();
		}else{
			String[] data = arrs.get(cards[nowround].get(0));
			int max = 0;
			choose = 1;
			for(int i=1;i<data.length;i++){
				if(Integer.parseInt(data[i])>=max){
					max = Integer.parseInt(data[i]);
					choose=i;
				}
			}
		}
		choose = choose>attrs.length? 1:choose;
		return choose;
	}
	public static int getWinner(int choose){
		int winner = 666;
		int max = 0;
		int token=0;
		for(int i=0;i<players.size();i++){
			if(cards[i].size()>0){
				String[] data = arrs.get(cards[i].get(0));
				int value = Integer.parseInt(data[choose]);

				if(value == max){
					winner = 666;
					isai = i>0? 1:0;
				}
				if(value > max){
					winner = i;
					max = value;
				}
				token++;
			}
		}
		end = token ==1? 1:0;
		return winner;
	}

	public static int getrand(){
		int rand = 0;
		List alive = new ArrayList();
		for(int i=0;i<players.size();i++){
			if(cards[i].size()>0){
				alive.add(i);
			}
		}
		Collections.shuffle(alive);
		Collections.shuffle(alive);
		rand = (Integer)alive.get(0);
		return rand;
	}

	public static void commonLog(FileOutputStream fos) throws IOException{
		StringBuffer aBuffer = new StringBuffer("");
		for(int i=0;i<commonCards.size();i++){
			aBuffer.append(arrs.get(commonCards.get(i))[0]+" ");
			if(i%4==0){
				aBuffer.append("\r\n");
			}
		}
		byte[] bytesArray = aBuffer.toString().getBytes();
		fos.write(bytesArray);
		fos.write("\r\n------------\r\n".getBytes());
		fos.flush();
	}

	public static void main(String[] args) {

		boolean writeGameLogsToFile = false; // Should we write game logs to file?
		if (args[0].equalsIgnoreCase("true")) writeGameLogsToFile=true; // Command line selection
		
		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the application
		
		// Loop until the user wants to exit the game
		while (!userWantsToQuit) {

			// ----------------------------------------------------
			// Add your game logic here based on the requirements
			// ----------------------------------------------------
			
			userWantsToQuit=true; // use this when the user wants to exit the game
			
		}


	}

}
