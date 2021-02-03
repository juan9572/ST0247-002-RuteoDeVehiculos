def perm(c):
    if len(c) == 0:
        return [[]]
    r = perm(c[:-1])
    return r +[s + [c[-1]] for s in r]

def prin(c):
    for e in  sorted(c, key=lambda s: (len(s), s)):
        print(e)

def combs():
    a = 'abcd'
    b = 'abcd'
    c = 'abcd'
    d = 'abcd'
    resultado = [x+y+z+w for x in a for y in b for z in c for w in d]
    print(resultado)

a = "abcd"
print("Permutaciones: ")
prin(perm(a))
print("Combinaciones: ")
combs()
