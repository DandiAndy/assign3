import java.io.*;
import java.util.*;

public class RuleMiner {
    
    public static String filename = "data1";
    public static double minConf = 20.00;
    public static double minSup = 50.00;
    public static String[] columns;

    public static void main(String[] args) throws IOException {
	Scanner scn  = new Scanner(System.in);
	
	// Get filename and stream into the reader.
	System.out.println("Please input the data file name: ");
	filename = scn.nextLine();
	FileInputStream fstream = null;
	BufferedReader bread = null;
	try {
	    fstream = new FileInputStream(filename);
	    bread = new BufferedReader(new InputStreamReader(fstream));
	    System.out.println("File Found.");
	} catch (Exception e){
	    System.err.println(e.getMessage());
	    System.out.println("Please input a proper file location.");
	    System.exit(0);
	}
	
	//Read the file line by line, split into tokens, and put into the data structure.
	Map<Integer, ArrayList<String>> data = new HashMap<Integer, ArrayList<String>>();
	String dataLine;
	int i = 0;
	while((dataLine = bread.readLine()) != null){
	    String[] dataItems = dataLine.split("\\s+");
	    
	    //get table names
	    if(i == 0){
		columns = dataItems;
	    }
	    //set ArrayList of for the HashMap
	    if(i != 0){
		ArrayList<String> list = new ArrayList<String>();
		int j = 0;
		for(String item : dataItems){
		    list.add(columns[j]+"="+item);
		    j++;
		}
		if(!list.isEmpty()){
		    System.out.println(list);
		    data.put(i, list);
		}
	    }
	    i++;
	}
	System.out.println(data.size());

	System.out.println("Please input the minimum support rate as a double (must be between 1-100): ");
	minSup = scn.nextDouble();
	if(minSup > 100 || minSup < 0){
	    System.out.println("Invalid minimum support rate. Range is 1-100. Setting support rate to default (20%).");
	}

	System.out.println("Please input the minimum confidence rate as a double (must be between 1-100): ");
        minConf = scn.nextDouble();
	if(minConf > 100 || minConf < 0){
            System.out.println("Invalid minimum confidence rate. Range is 1-100. Setting support rate to default (50%).");
        }
	
	BuildFirstItemset(data);
     }

    public static Map<String, Double> BuildFirstItemset(Map<Integer, ArrayList<String>> T){
	Map<String, Double> supMap = new LinkedHashMap<String, Double>();
	for (int i = 1; i < T.size() - 1; i++){
	    ArrayList<String> list = T.get(i);
	    for(String term : list){
		if(supMap.containsKey(term)){
		    supMap.put(term, supMap.get(term) + (1.0/T.size()));
		}else{
		    supMap.put(term, 1.0/T.size());
		}
	    }
	}

	System.out.println("Before: " + supMap);
	ArrayList<String> terms = new ArrayList<String>(supMap.keySet());
	for(String term : terms){
	    if(supMap.get(term) < minSup/100){
		supMap.remove(term);
	    }
	}
	System.out.println("After: " + supMap);

	return supMap;
    }

    public static Map<String, Double> Apriori(Map<String, Double> T){
	
	//while()
	return T;
    }
}
