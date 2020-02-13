package commandline;

import java.io.*;
import java.math.*;
import java.util.*;
import game.gamePlay;

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
	static int com;
	static int lastwin;
	static int draw=0;
	static String realcard[][];

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

	public static void recoedLog(FileOutputStream fos, String user, String[] card) throws IOException{
		String str = "";
		for(int i=1;i<attrs.length;i++){
			str+=attrs[i]+": "+card[i];
		}
		fos.write((user+"'s Card used: "+card[0]+str).getBytes());
		fos.write("\r\n----------\r\n".getBytes());
		fos.flush();
	}



	/**
	 * This main method is called by TopTrumps.java when the user specifies that they want to run in
	 * command line mode. The contents of args[0] is whether we should write game logs to a file.
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) {

		boolean writeGameLogsToFile = false; // Should we write game logs to file?
		if (args[0].equalsIgnoreCase("true")) writeGameLogsToFile=true; // Command line selection
		File logfile;
		log = writeGameLogsToFile;
		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the application
		/*
		// Loop until the user wants to exit the game
		while (!userWantsToQuit) {

			// ----------------------------------------------------
			// Add your game logic here based on the requirements
			// ----------------------------------------------------

			userWantsToQuit=true; // use this when the user wants to exit the game

		}
		*/

		try {
			getCards();
			if(writeGameLogsToFile){
				logfile = new File("toptrumps.log");

				fos = new FileOutputStream(logfile);

				/* test if exists or delete it. */
				logfile.delete();
				if (!logfile.exists()) {
					logfile.createNewFile();
				}
				fos.write("All cards\r\n".getBytes());
				StringBuffer sBuffer = new StringBuffer("");
				for(int i=0;i<arrs.size();i++){
					sBuffer.append(arrs.get(i)[0]+" ");
					if(i%4==0){
						sBuffer.append("\r\n");
					}

				}

				byte[] bytesArray = sBuffer.toString().getBytes();

				fos.write(bytesArray);
				fos.write("\r\n----------\r\n".getBytes());
				fos.flush();
			}

			num=0;
			num = arrs.keySet().size();

			com  = num%(allplayer);
			pernum = (num-com)/allplayer;

			int round=1;
			int nowround;
			int score[];
			int attr_index=0;
			String now_arr[];
			score = new int[allplayer];
			while(true){
				menu();
				Scanner sc = new Scanner(System.in);
				int opt = sc.nextInt();
				if( opt == 1 ){
					int[] data = game.gamePlay.get();
					print("Number of Games: "+data[0]);
					print("Number of Human wins: "+data[1]);
					print("Number of AI Wins: "+data[2]);
					print("Average Draws per game: "+data[3]);
					print("Longest Game: "+data[4]);
					continue;
				}else{
					end = 0;
					draw = 0;
					players = new HashMap<Integer,String>();
					players.put(0,"You");
					score[0]=0;
					round=1;
					for(int pa=1;pa<=aiplayers;pa++){
						players.put(pa,"AI Players "+pa);
						score[pa]=0; //all players never have won
					}
					nowround  = (int) Math.floor(Math.random()*players.size());
					print("Game Start");
					//
					shuffle(); // get new cards
					if(writeGameLogsToFile){
						StringBuffer aBuffer = new StringBuffer("");
						for(int i=0;i<players.size();i++){
							fos.write((players.get(i)+"\r\n").getBytes());
							aBuffer = new StringBuffer("");
							for(int j=0;j<cards[i].size();j++){
								aBuffer.append(arrs.get(cards[i].get(j))[0]+" ");
								if(j%4==0){
									aBuffer.append("\r\n");
								}
							}
							byte[] bytesArray;// = aBuffer.toString().getBytes();
							bytesArray = aBuffer.toString().getBytes();
							fos.write(bytesArray);
							fos.write("\r\n----------\r\n".getBytes());
						}
						fos.flush();
					}
					while(end!=1){
						print("Round "+round);
						print("Round "+round+": Players have drawn their cards");
						if(cards[0].size()==0){
							print("You have Lost!");
						}else{
							now_arr = arrs.get(cards[0].get(0));
							print("You drew '"+now_arr[0]+"':");
							for(attr_index=1;attr_index<attrs.length;attr_index++){
								print("   > "+attrs[attr_index]+": "+now_arr[attr_index]);
							}
							print("There are '"+(cards[0].size()-1)+" cards in your deck");
						}
						if(writeGameLogsToFile){
							for(int i=0;i<players.size();i++){
								if(cards[i].size()>0){
									fos.write((players.get(i)+"'s Crad Playing:"+arrs.get(cards[i].get(0))[0]+"\r\n").getBytes());
									fos.write("\r\n----------\r\n".getBytes());
								}
							}
							fos.flush();
						}
						int choose = showChoose(sc,nowround);
						if(writeGameLogsToFile){
							if(cards[nowround].size()>0){
								fos.write((players.get(nowround)+"'s category selected: "+attrs[choose]+"\r\n"
										+"corresponding values: "+arrs.get(cards[nowround].get(0))[choose]+"\r\n").getBytes());
								fos.write("\r\n----------\r\n".getBytes());
							}

							fos.flush();
						}
						int winner = getWinner(choose);
						if(winner <666){
							lastwin = (Integer) cards[winner].get(0);
							score[winner]++;
						}
						for(int ci = 0;ci<allplayer;ci++){
							if(cards[ci].size()>0){
								commonCards.add(cards[ci].get(0));
								if(writeGameLogsToFile){
									commonLog(fos);
									recoedLog(fos,players.get(ci),arrs.get(cards[ci].get(0)));
								}
								cards[ci].remove(0);
							}else{
								continue;
							}
						}

						if(winner == 666){
							draw++;
							nowround = isai==1?getrand():(cards[0].size()>0?0:getrand());
							//commonCards.add("");
							print("Round "+round+": This round was a Draw, common pile now has "+commonCards.size()+" cards");
							//print(commonCards.toString());
						}else{
							nowround = winner;
							for(int ci=0;ci<commonCards.size();ci++){
								cards[winner].add(commonCards.get(ci));
							}
							commonCards = new ArrayList();

							print("Round "+round+": Player "+players.get(winner)+" won this round");
							print("The winning card was '"+arrs.get(lastwin)[0]+"':");
							for(int ij=1;ij<attrs.length;ij++){
								print("   > "+attrs[ij]+": "+arrs.get(lastwin)[ij]+" "+(ij==choose?"<--":""));
							}
						}

						round++;

					}
					print("Game End");
					print("The overall winnder was "+players.get(lastwin));
					gamePlay.update(true, score, draw, round, lastwin);
					if(writeGameLogsToFile){
						StringBuffer aBuffer = new StringBuffer("");
						fos.write(("The last winner is: "+players.get(lastwin)).getBytes());
						fos.write("\r\n----------\r\n".getBytes());
						fos.flush();
					}
					print("Scores:");
					for(int i=0;i<score.length;i++){
						print("   "+players.get(i)+": "+score[i]);
					}
					continue;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Where is StarCitizenDeck.txt?");
			e.printStackTrace();
		}
	}
}
