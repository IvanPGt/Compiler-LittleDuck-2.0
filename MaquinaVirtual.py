class quad:
    def __init__(self, oper, opdo1, opdo2, res):
        self.oper = int(oper)
        self.opdo1 = int(opdo1)
        self.opdo2 = int(opdo2)
        self.res = int(res)

archivo = open("cruadruplos.txt", 'r')

mem_size = int(archivo.readline().strip())
        
ctes = int(archivo.readline().strip())

memoria = [mem_size]

memoria[0] = -1

for _ in range(ctes):
    cte = archivo.readline().strip().split()
    tipo = cte[0]
    valor = cte[1]
    dir = int(cte[2])
    
    if tipo == 0:
        valor = int(valor)
    elif tipo == 1:
        valor = float(valor)

    memoria[dir] = valor

quads = []
for line in archivo:
    valores = line.strip().split()
    quad = quad(valores[0], valores[1], valores[2], valores[3])
    quads.append(quad)

index = 0
while index != len(quads):

    match index:
            case 0:
                memoria[quads[index].res] = memoria[quads[index].opdo1] * memoria[quads[index].opdo2]
                index += 1
                break
            case 1:
                memoria[quads[index].res] = memoria[quads[index].opdo1] / memoria[quads[index].opdo2]
                index += 1
                break 
            case 2:
                memoria[quads[index].res] = memoria[quads[index].opdo1] + memoria[quads[index].opdo2]
                index += 1
                break 
            case 3:
                memoria[quads[index].res] = memoria[quads[index].opdo1] - memoria[quads[index].opdo2]
                index += 1
                break 
            case 4:
                memoria[quads[index].res] = memoria[quads[index].opdo1] < memoria[quads[index].opdo2]
                index += 1
                break 
            case 5:
                memoria[quads[index].res] = memoria[quads[index].opdo1] > memoria[quads[index].opdo2]
                index += 1
                break 
            case 6:
                memoria[quads[index].res] = memoria[quads[index].opdo1] != memoria[quads[index].opdo2]
                index += 1
                break 
            case 7:
                break 
            case 8:
                break 
            case 9:
                break 
            case 10:
                break 
            case 11:
                break 
            
            case _:
                break #default