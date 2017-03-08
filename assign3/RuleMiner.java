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
		    data.put(i, list);
		}
	    }
	    i++;
	}


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
	
	Map<String, Double> k1 = BuildFirstItemset(data);
	//System.out.println(k1);
	//System.out.println("");
	Map<ArrayList<String>, Double> itemsets = Apriori(k1, data);
	System.out.println(itemsets);
     }

    public static Map<String, Double> BuildFirstItemset(Map<Integer, ArrayList<String>> T){
	Map<String, Double> supMap = new LinkedHashMap<String, Double>();
	for (int i = 1; i <= T.size(); i++){
	    ArrayList<String> list = T.get(i);	
	    for(String term : list){
		if(supMap.containsKey(term)){
		    supMap.put(term, supMap.get(term) + (1.0/T.size()));
		}else{
		    supMap.put(term, 1.0/T.size());
		}
	    }
	}

	ArrayList<String> terms = new ArrayList<String>(supMap.keySet());
	for(String term : terms){
	    if(supMap.get(term) < minSup/100){
		supMap.remove(term);
	    }
	}
	//System.out.println(supMap);
	return supMap;
    }

    public static Map<ArrayList<String>, Double> Apriori(Map<String, Double> k1, Map<Integer, ArrayList<String>> T){
	Map<ArrayList<String>, Double> results = new LinkedHashMap<ArrayList<String>, Double>();
	
	//Add k1 to results

	ArrayList<ArrayList<String>> C = new ArrayList<ArrayList<String>>();
	Iterator it = k1.entrySet().iterator();
	//Create List of lists using the k1 map.
	//Add k1 to results
	ArrayList<String> l = new ArrayList<String>();
	while (it.hasNext()) {
	    Map.Entry pair = (Map.Entry)it.next();
	    l = new ArrayList<String>();
	    l.add((String)pair.getKey());
	    results.put(l,(Double)pair.getValue());
	    C.add(l);
	    it.remove();
	}
	//Sort the k1 list to be in order. Since each inner arraylist contains 1 string, we only need to check index 0.
	Collections.sort(C, new Comparator<ArrayList<String>>() {
		@Override
		public int compare(ArrayList<String> a1, ArrayList<String> a2) {
		    return a1.get(0).compareTo(a2.get(0));
		}
	});
	ArrayList<ArrayList<String>> Cx = new ArrayList<ArrayList<String>>();
	//Index used to make the subList
	int i = 1;
	//All list not containing the same initial value of the init previous list value found need to be skipped to avoid unordered dublicates. 
	String initValue = "";
	//Add a temp arraylist to hold new itemlist
	ArrayList<String> temp1 = new ArrayList<String>();
	ArrayList<String> temp2 = new ArrayList<String>();
	while(C.size() > 1){
	    //Now we can find Ck item set using Ck-1
	    // System.out.println(C);
	    Collections.sort(C, new Comparator<ArrayList<String>>() {
                @Override
	        public int compare(ArrayList<String> a1, ArrayList<String> a2) {
                    return a1.get(0).compareTo(a2.get(0));
                }
	    });
	    
	    
	    if(C.size() > 1){
		for(ArrayList<String> itemlist : C){
		    initValue = itemlist.get(0);
		    for(ArrayList<String> next : C.subList(i, C.size())){
			//k1 can't have the same features given they are all seperated. Needs a special case.
			if(itemlist.size() < 2){
			    temp1 = new ArrayList<String>(itemlist);
                            temp2 = new ArrayList<String>(next);
                            temp2.removeAll(temp1);
                            temp1.add(temp2.get(0));
                            if(!Cx.contains(temp1)){
                                Cx.add(new ArrayList<String>(temp1));
                            }
                            temp1.clear();
                            temp2.clear();
			//if the initial value of the itemlist is not the same as the initial value of the next list we can skip that next list.
			}else if(next.get(0) == initValue){
			    temp1 = new ArrayList<String>(itemlist);
			    temp2 = new ArrayList<String>(next);
			    temp2.removeAll(temp1);
			    if(!temp2.isEmpty()){
				temp1.add(temp2.get(0));

				//No need to remove sets with duplicate columns as they will be filtered out in the pruning stage.
				if(!Cx.contains(temp1)){
				    if(temp1.size()> 2){
				    //    System.out.println(temp1);
				    }
				    Cx.add(new ArrayList<String>(temp1));
				}
			    }
			    temp1.clear();
			    temp2.clear();	
			}
		    }
		    i++;
		}
		i = 1;
		C = new ArrayList<ArrayList<String>>(Cx);
		//System.out.println(C);
		//System.out.println("");
		//System.out.println(Cx);
		//System.out.println("");
		Cx.clear();
	    }

	    //Now we can prune the set by looking through the transactions and the itemsets in C.
	    Boolean b = true;
	    ArrayList<ArrayList<String>> Cy = new ArrayList<ArrayList<String>>();
	    for(ArrayList<String> itemlist : C){
		//System.out.println(itemlist);
		for(int a = 1; a <= T.size(); a++){
		    ArrayList<String> datalist = T.get(a);
		    b = true;
		    for(String term : itemlist){
			if(!datalist.contains(term)){
			    b = false;
			    break;
			}
		    }
		    if(b){
			if(results.containsKey(itemlist)){
			    //if(itemlist.size() > 2){
				//System.out.println(datalist);
				//}
			    results.put(itemlist, results.get(itemlist) + (1.0/T.size()));
			}else{
			    results.put(itemlist, 1.0/T.size());
			    Cy.add(itemlist);
			    //if(itemlist.size()>2){
			    	//System.out.println(itemlist);
				//}
			}
		    }
		}
	    }
	    ArrayList<ArrayList<String>> itemlists = new ArrayList<ArrayList<String>>(results.keySet());
	    for(ArrayList<String> itemset : itemlists){
		if(results.get(itemset) < minSup/100){
		    results.remove(itemset);
		    Cy.remove(itemset);
		}
	    }
	    C = new ArrayList<ArrayList<String>>(Cy);
	}
	return results;
    }
}
