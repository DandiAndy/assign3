#  make apriori from apriori.c

CC=cc
CFLAGS= -O
all: apriori

apriori:  apriori.o calc_itemsets.o calc_rules.o linked_list.o load_datafile.o output.o
	$(CC) -o apriori apriori.o calc_itemsets.o calc_rules.o linked_list.o load_datafile.o output.o -lm
	rm *.o

apriori.o:  apriori.c apriori.h 
	$(CC) -c apriori.c

calc_itemsets.o:  calc_itemsets.c apriori.h 
	$(CC) -c calc_itemsets.c

calc_rules.o:  calc_rules.c apriori.h 
	$(CC) -c calc_rules.c

linked_list.o:  linked_list.c apriori.h 
	$(CC) -c linked_list.c
	
load_datafile.o:  load_datafile.c apriori.h 
	$(CC) -c load_datafile.c
	
output.o:  output.c apriori.h 
	$(CC) -c output.c
	
