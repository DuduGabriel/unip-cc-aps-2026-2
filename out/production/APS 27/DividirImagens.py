from PIL import Image
import os
from MySQL import salvarBanco

linhas = 3
colunas = 3

def cortarImagens(caminho_imagem, imagemAtual, dados_Arquivo):
    arquivo = os.path.join(os.getcwd(), caminho_imagem)
    caminho_saida = os.path.join(os.getcwd(), "../../Imagens", "Recortes")

    imagem = Image.open(arquivo).convert("RGB")

    largura, altura = imagem.size

    for row in range(linhas):
        for col in range(colunas):
            esquerda = col * largura // colunas
            direita = (col + 1) * largura // colunas
            topo = row * altura // linhas
            baixo = (row + 1) * altura // linhas

            novaImagem = imagem.crop((esquerda, topo, direita, baixo))

            arquivo_saida = os.path.join(caminho_saida, f"imagem X{imagemAtual}_{row + 1}_{col + 1}.png")

            novaImagem.save(arquivo_saida)

            dados_Arquivo.append(arquivo_saida)
            dados_Arquivo.append(os.path.getsize(arquivo_saida))

            salvarBanco(dados_Arquivo)