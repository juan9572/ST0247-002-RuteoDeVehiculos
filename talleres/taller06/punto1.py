def cambioGreedy(cambio,denominaciones):
    denominaciones.sort()
    solucion = 0
    indice = len(denominaciones) - 1
    monedas = []
    while indice >= 0:
        if (denominaciones[indice] + solucion) <= cambio:
            solucion = solucion + denominaciones[indice]
            monedas.append(denominaciones[indice])
        else:
            indice = indice-1
    print(monedas)
denominaciones = [1,5,10,25,50,100]
cambioGreedy(110, denominaciones)
