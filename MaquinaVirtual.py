class Quad:
    def __init__(self, oper, opdo1, opdo2, res):
        self.oper = int(oper)
        self.opdo1 = int(opdo1)
        self.opdo2 = int(opdo2)
        self.res = int(res)

archivo = open("cruadruplos.txt", 'r')

mem_size = int(archivo.readline().strip())
        
ctes = int(archivo.readline().strip())

memoria = [None] * mem_size

memoria[0] = -1

for _ in range(ctes):

    tipo = int(archivo.readline().strip())
    valor = archivo.readline().strip()
    dir = int(archivo.readline().strip())
    
    if tipo == 0:
        valor = int(valor)
    elif tipo == 1:
        valor = float(valor)
    elif tipo == 2:
        valor = valor.strip('"')

    memoria[dir] = valor

quads = []

for line in archivo:
    valores = line.strip().split()
    quad = Quad(valores[0], valores[1], valores[2], valores[3])
    quads.append(quad)

index = 0
while index != len(quads) and index != None:
    match quads[index].oper:
            case 0:
                memoria[quads[index].res] = memoria[quads[index].opdo1] * memoria[quads[index].opdo2]
                index += 1
              
            case 1:
                memoria[quads[index].res] =  memoria[quads[index].opdo2] / memoria[quads[index].opdo1]
                index += 1
        
            case 2:
                memoria[quads[index].res] = memoria[quads[index].opdo1] + memoria[quads[index].opdo2]
                index += 1
           
            case 3:
                memoria[quads[index].res] = memoria[quads[index].opdo2] - memoria[quads[index].opdo1] 
                index += 1
        
            case 4:
                memoria[quads[index].res] = memoria[quads[index].opdo2] < memoria[quads[index].opdo1] 
                index += 1
         
            case 5:
                memoria[quads[index].res] = memoria[quads[index].opdo2] > memoria[quads[index].opdo1]
                index += 1
          
            case 6:
                memoria[quads[index].res] = memoria[quads[index].opdo1] != memoria[quads[index].opdo2]

                index += 1
            
            case 7:
                memoria[quads[index].res] = memoria[quads[index].opdo1]
                index += 1
           
            case 8:
                print(memoria[quads[index].res], end="")
                index += 1
          
            case 9:
                index = quads[index].res
              
            case 10:
                if not memoria[quads[index].opdo1]:
                    index = quads[index].res
                else:
                    index += 1
             
            case 11:
                if memoria[quads[index].opdo1]:
                    index = quads[index].res
                else:
                    index += 1
      
            
            case _:
                break #default