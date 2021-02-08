global N
N = 4

def impTablero(tablero):
    for i in range(N):
        for j in range(N):
            print(tablero[i][j])
        print

def esSeguro(tablero, fila, col):
    for i in range(col):
        if tablero[fila][i] == 1:
            return False
    
    for i, j in zip(range(fila, -1, -1), range(col, -1, -1)): 
        if board[i][j] == 1: 
            return False
    for i, j in zip(range(row, N, 1), range(col, -1, -1)): 
        if board[i][j] == 1: 
            return False
    return True
def nReinas(tablero, columnas):
    if col == N:
        impTablero(tablero)
        return True
    res = False
    for i in range (N):
        if(esSeguro(tablero, i, col)):
            tablero[i][col] = 0
            res = nReinas(tablero, col + 1) or res
