class GraphAL:
    def __init__(self, size):
        self.size = size
        self.lista = [[] for i in range(size)]


    def addArc(self, vertex, edge, weight):
        self.lista[vertex].append((edge,weight))

    def getSuccessors(self, vertice):
        sucesores = []
        for s in self.lista[vertice]:
            sucesores.append(s[0])
        return sucesores


    def getWeight(self, source, destination):
        for i in self.lista[source]:
            if i[0] == destination:
                return i[1]


    def __str__(self):
        return '{}'.format(self.lista)

def main():
    ga = GraphAL(3)
    ga.addArc(0, 3, 10)
    print(ga)
    print(ga.getWeight(0, 3))
    ga.addArc(0, 4, 7)
    print(ga)
    print(ga.getSuccessors(0))

main()