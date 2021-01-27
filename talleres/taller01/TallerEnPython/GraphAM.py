import numpy as vector

class GraphAm:

    def __init__(self, size):
        self.size = size
        ##global matriz
        self.matriz = vector.zeros((size,size), dtype=int)

   ## def getEdges(self):


    def getWeight(self, source, destination):
        return self.matriz[source][destination]


    def addArc(self, source, destination, weight):
        self.matriz[source][destination] = weight

    def getSuccessors(self, vertex):
        lista = []
        for i in range(len(self.matriz[vertex])):
            if self.matriz[vertex][i] != 0:
                lista.append(i)
        return lista


    def __str__(self):
         return f'{self.matriz}'

def main():
    ga = GraphAm(3)
    ga.addArc(0, 1, 15)
    ga.addArc(0, 2, 30)
    ga.addArc(1, 1, 7)
    print(ga)
    print(ga.getSuccessors(0))
    print(ga.getWeight(1, 1))

main()