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
