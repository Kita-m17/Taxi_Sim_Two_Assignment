taxi dispatch simulator

Overview:
The Taxi Dispatch Simulator is a Java program that simulates a taxi dispatch system. It accepts input from a file containing data on roads, shop locations, and incoming calls, and calculates and outputs details for the best (shortest trip) for each call.
In my implementation, I added the advance features such as taxis are not always stationed at shops. Additionally, each taxi and shop belong to different companies whereby whereby taxis operate between shops of their own company, and for each shop, the program will find the shortest path to that shop of the same company. The number of taxis are also finite where by there are only a specific number of taxi traveling to a shop of a specific company.

The input will be of the form:
<number of nodes><newline>
{<source node number> {<destination node number> <weight>}*<newline>}*
<number of taxis>*<newline>
{<taxi node number>{<company>}*<newline>}
<number of shops><newline>
{<shop node number>{<company>}*<newline>}
<number of clients><newline>
{<client node number>*<newline>
(‘{x}*’ mean zero or more of item x.)

There is at least one edge leaving every node. The last line is a chronologically ordered series of calls, where each call is represented by the number of the node that the client calls a taxi.

Example of input:
6
0 4 15
1 0 14 2 7 3 23
2 0 7
3 1 23 4 16
4 2 15 3 9
5 1 4 2 3
4
1:ShopNice 2:TakeNPurchase 3:ShopNice 5:QNQ
3
0:QNQ 3:ShopNice 5:QNQ
3
1 4 2

Output:
client 1
destination QNQ
taxi 5
5 1 
shop 0
multiple solutions cost 14
client 4
destination ShopNice
taxi 3
3 4 
shop 3
4 3 
client 2
destination QNQ
taxi 5
5 2 
shop 0
2 0

The code consists of the following main components:

SimulatorTwo Class: This class contains methods for loading data from a file, processing client calls, and finding the nearest taxis and shops for each client location.
Graph Class: This class represents the graph structure used to model the network of taxis and shops. It includes methods for adding vertices and edges, calculating shortest paths, and performing Dijkstra's algorithm.
Company Class: This class represents a taxi company or shop, with attributes for ID and company name.

Author: Nikita Martin
Date: 20-03-2024
