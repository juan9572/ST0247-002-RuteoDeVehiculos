import numpy as vector

class GraphAm:

    def __init__(self, size):
        self.size = size
        global matriz
        matriz = vector.zeros((size,size), dtype=int)

   ## def getEdges(self):


    def getWeight(self, source, destination):
        return matriz[source][destination]


    def addArc(self, source, destination, weight):
        matriz[source][destination] = weight

    def getSuccessors(self, vertex):
        lista = []
        for i in range(len(matriz[vertex])):
            if matriz[vertex][i] != 0:
                lista.append(i)
        return lista


    ## def __str__(self):

def main():
    prueba = GraphAm(7)
    prueba.addArc(3, 5, 35)
    prueba.addArc(3, 2, 65)
    prueba.addArc(3, 3, 65)
    print(prueba.getSuccessors(3))
    print(prueba.getWeight(3, 3))

main()