import sys
 
class Graph():
 
    def __init__(self, vertices):
        self.V = vertices
        self.graph = [[0 for column in range(vertices)]
                      for row in range(vertices)]
 
    def printSolution(self, distancia):
        print("Vertice cuesta Distancia desde fuente")
        for nodo in range(self.V):
            print(nodo, "cuesta", distancia[nodo])
 
    def minDistance(self, distancia, sptSet):
 
        min = sys.maxsize
 
        for v in range(self.V):
            if distancia[v] < min and sptSet[v] == False:
                min = distancia[v]
                min_index = v
 
        return min_index
 
    def dijkstra(self, source):
 
        distancia = [sys.maxsize] * self.V
        distancia[source] = 0
        sptSet = [False] * self.V
 
        for cout in range(self.V):
 
            
            u = self.minDistance(distancia, sptSet)
 
            sptSet[u] = True

            for v in range(self.V):
                if self.graph[u][v] > 0 and sptSet[v] == False and distancia[v] > distancia[u] + self.graph[u][v]:
                    distancia[v] = distancia[u] + self.graph[u][v]
 
        self.printSolution(distancia)
 
g = Graph(9)
g.graph = [[0, 4, 0, 0, 0, 0, 0, 8, 0],
           [4, 0, 8, 0, 0, 0, 0, 11, 0],
           [0, 8, 0, 7, 0, 4, 0, 0, 2],
           [0, 0, 7, 0, 9, 14, 0, 0, 0],
           [0, 0, 0, 9, 0, 10, 0, 0, 0],
           [0, 0, 4, 14, 10, 0, 2, 0, 0],
           [0, 0, 0, 0, 0, 2, 0, 1, 6],
           [8, 11, 0, 0, 0, 0, 1, 0, 7],
           [0, 0, 2, 0, 0, 0, 6, 7, 0]
           ]
 
g.dijkstra(8)
