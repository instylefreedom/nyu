import java.io.*;
import java.util.*;

class twopass{
	//second pass 
	static ArrayList<String> def = new ArrayList<String>();
	static ArrayList<Integer> defnn = new ArrayList<Integer>();
	static ArrayList<String> use = new ArrayList<String>();
	static ArrayList<String> local = new ArrayList<String>();
	static ArrayList<Integer> localn = new ArrayList<Integer>();
	static ArrayList<Integer> usen = new ArrayList<Integer>();
	static ArrayList<String> text = new ArrayList<String>();
	static ArrayList<Integer> textn = new ArrayList<Integer>();
	static ArrayList<Integer> error = new ArrayList<Integer>();

	static String type = "";
	static int location = 0;

	
	//first pass 
	//def name
	static ArrayList<String> variable = new ArrayList<String>();
	//def final address 
	static ArrayList<Integer> variablea = new ArrayList<Integer>();
	static ArrayList<Integer> check = new ArrayList<Integer>();
	//relative address for relative address later on
	static ArrayList<Integer> relativebase = new ArrayList<Integer>();
	static int module = 0;
	static int defnum = 0;
	static int usenum = 0;
	static int textnum = 0;

	public static void firstpass( Scanner list){
		String n = "";String uf = "";int un = 0;String tf = "";int tn = 0;

		//first # is # of modules 1st loop		
		String hold = list.next();
		module = Integer.parseInt(hold);		
		int base =0;
		int rel =0;
		int temp =0;
		 
		System.out.printf("Symbol Table \n");
		// iterate module #		
		for(int i = 0; i < module; i++){
			hold = list.next();
			defnum = Integer.parseInt(hold);
			
			// iterate each def #
			
			for(int x = 0; x < defnum; x++){
				String defn = list.next();
				rel = list.nextInt();
				def.add(defn);
				defnn.add(rel);
				if(variable.indexOf(defn) != -1){
					temp = rel + base;
					int wrong = variable.indexOf(defn);
					check.set(wrong, 1);
					variablea.set(wrong,temp);


					//System.out.print("Error, symbol multiply defined, last value will be printed");
				}

				else{
					temp = rel + base;
					variablea.add(temp);
					variable.add(defn);
					check.add(0);

				}
			}
			// iterate use
			usenum = list.nextInt();
			for(int z = 0; z<usenum;z++){
				uf = list.next();
				use.add(uf);
				un = list.nextInt();
				usen.add(un);
				if(usen.indexOf(un)!= -1){

				}
			}

			// iterate text and add to base
			textnum = list.nextInt();
			base += textnum;
			for(int s = 0; s<textnum;s++){
				tf = list.next();
				text.add(tf);
				tn = list.nextInt();
				textn.add(tn);
				error.add(0);

			}


			
		}
		String finalv ="";
		int finaln = 0;
		for(int q = 0; q<variable.size();q++){
			if (check.get(q)==0){
				finalv = variable.get(q);
				finaln = variablea.get(q);
				System.out.printf("%s = %d \n", finalv, finaln);
			}
			else{
				finalv = variable.get(q);
				finaln = variablea.get(q);
				System.out.printf("%s = %d Error: This variable is multiply defined; last value used. \n", finalv, finaln);
			}
		}
		//list.close();		
	}
	public static void secondpass(Scanner list){
		int count = 0;
		int base = 0;
		int allindex=0;
		String a = list.next();
		module = Integer.parseInt(a);


		for(int i = 0; i < module; i++){
			a = list.next();
			defnum = Integer.parseInt(a);
			
			// iterate each def #
			
			for(int x = 0; x < defnum; x++){
				String defn = list.next();
				list.next();
			}
			// iterate use
			int index = 0;
			usenum = list.nextInt();
			for(int z = 0; z<usenum;z++){
				String usedef = list.next();
				local.add(usedef);
				int lap = list.nextInt();
				//figure out mulitple symbol use same text
				if(localn.indexOf(lap)!=-1){
					//multiple symbol use!
					//removed! 
					int no = localn.indexOf(lap);
					//local.remove(no);
					localn.set(no,100);
					int fix = base+no+1;
					error.set(fix, 2);

				}
				localn.add(lap);
				if(variable.indexOf(usedef) == -1){
					//error meesage and all change to 111;

				} 			
			}
			// iterate text and add to base
			textnum = list.nextInt();
			int number=0;
			int first=0;
			int last=0;
			int remain=0;
			int indexx = 0;
			for(int s = 0; s<textnum;s++){
				type = list.next();
				location = list.nextInt();

				if(type.equals("A")){
					number = location;
					first = location/1000;
					last = isolate(location,1);
					remain = location%1000;
					if (remain>=300){
						number = first*1000+299;
						textn.set(count,number);
						error.set(count,1);
					}
				}
				else if(type.equals("R")){
					number = location;
					first = location/1000;
					last = isolate(location,1);
					remain = location%1000;
					if(remain>textnum){
						number = first*1000+base;
						textn.set(count,number);
						error.set(count, 3);
					}
					else{
						number = location += base;
						textn.set(count,number);
					}

				}
				else if(type.equals("E")){
					number = location;
					first = location/1000;
					last = isolate(location,1);
					remain = location%1000;
					//if the E is picked in use
					if(localn.indexOf(indexx)!= -1){
						//gotta check if use is in variable
						int leggo = localn.indexOf(indexx);
						String git = local.get(leggo);
						if(variable.indexOf(git)!= -1){
						if(remain == 777){
							//get var name
							int coor = localn.indexOf(indexx);
							String d = local.get(coor);
							int f = variable.indexOf(d);
							int yes = variablea.get(f);
							int change = first*1000+yes;
							textn.set(count,change);
						}
						else{
								//changes the current address
								int coor = localn.indexOf(indexx);
								String d = local.get(coor);
								int f = variable.indexOf(d);
								int yes = variablea.get(f);
								int change = first*1000+yes;
								textn.set(count,change);
							//jumps to somewhere
							while(remain!=777){
								//now i go to where the remain leads
								int jump = base+remain;
								number = textn.get(jump);
								first = number/1000;																
								int exx = first*1000+yes;
								textn.set(jump,exx);
								remain = number%1000;


							}
						}
					}
					else{
						if(remain == 777){
							//get var name
							int coor = localn.indexOf(indexx);
							String d = local.get(coor);
							int f = variable.indexOf(d);
							int yes = 111;
							int change = first*1000+yes;
							textn.set(count,change);
							error.set(count,4);
						}
						else{
								//changes the current address
								int coor = localn.indexOf(indexx);
								String d = local.get(coor);
								int f = variable.indexOf(d);
								int yes = 111;
								int change = first*1000+yes;
								textn.set(count,change);
								error.set(count,4);
							//jumps to somewhere
							while(remain!=777){
								//now i go to where the remain leads
								int jump = base+remain;
								number = textn.get(jump);
								first = number/1000;																
								int exx = first*1000+yes;
								textn.set(jump,exx);
								error.set(jump,4);
								remain = number%1000;



							}
						}						

					}						
					}


				}
			count++;indexx++;
			}
			indexx=0;
			base += textnum;
			local.clear();
			localn.clear();
		

		}

		System.out.printf("\n Memory Map ");
		String finalv ="";
		int finaln = 0;
		for(int q = 0; q<textn.size();q++){			
			finaln = textn.get(q);
			System.out.printf("\n %d ",  finaln);
			int er = error.get(q);
			if(er == 1){
				System.out.printf(" Error: A type address exceeds machine size; max legal value used");
			}
			else if(er == 2){
				System.out.printf(" Error: Multiple symbols used here; last one used");
			}
			else if(er == 3){
				System.out.printf(" Error: Type R address exceeds module size: 0 (relative) used");
			}
			else if(er == 4){
				System.out.printf(" Error: X21 is not defined; 111 used.");
			}			
		}
		for(int r = 0; r<variable.size();r++){
			String find = variable.get(r);
			if(use.indexOf(find)==-1){
				System.out.printf("\n Warning: %s was defined but never used.", find);
			}
		}


	}

	public static int isolate(int number, int digitPosition){
		return (number / (int) Math.pow(10, digitPosition - 1)) % 10;
	}	
			



		// at text += # for base, and += base to R address
	
		// after iteration, make changes to the hash table and print the Symbol table

		//second iteration to adjust the E  address and figure out errors 

		// compare hash to find multiple, and var exist both side 

		// If A 3digit>300 error


	public static void main(String[] args){
		Scanner list = new Scanner(System.in);
		//Scanner list2 = new Scanner(System.in);


		firstpass(list);
		secondpass(list);

	}





	
}