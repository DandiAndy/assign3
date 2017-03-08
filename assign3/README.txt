<COMPILATION>

To compile the program the user must run the following commands in this order
(Make sure you have read, write permissions on the file):

'make'
'java RuleMiner'

This will make the executable and run it. 

<OVERALL DESIGN>

The following is a description of the program and it's methods. Using Java, 
the association rule miner was created in one file containing 4 methods. The
main() method takes asks for a filename, min confidence, and min support. The 
file with with requested path is found and is divided into a list of list 
containing each tuple of transaction data. This data is then concatentated 
with it's column name to create the data we use. We then pass this data into 
the BuildFirstItemset() method. The build first method creates a Hashmap with 
the 1-itemsets and their corresponding support values. This 1-itemset is sent
into the Apriori() method. The Apriori method is responsible for finding the
2-3-...-n-itemsets and calculating their corresponding supports. Those itemsets
that don't meet the min support value are pruned. Once complete,another Hashmap
with all of <itemsets, Support> with the minimum support levels is made and 
tranfered to the final method. The final method, CreateRules(), creates the 
rules using the calculated support of each Hashmap couple. The created rules 
are printed and saved in the 'Rules' file.

<TASK PARTITION>

Andrew Kenny was responsible for the creation of the main() method, the 
BuildFirstItemset(), and the Apriori() method.

Hamza Awan was responsible for the CreateRules() method. 




