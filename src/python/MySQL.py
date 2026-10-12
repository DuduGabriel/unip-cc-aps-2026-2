import mysql.connector

def conectar():
    return mysql.connector.connect(
        host = "localhost",
        user = "root",
        password = "",
        database = "aps_26_2"
    )


def salvarBanco(dados):
    print("SALVANDO NO BANCO")

    DATA_CAPTURA = dados[0]
    LATITUDE = dados[1]
    LONGITUDE = dados[2]
    NOME_ARQUIVO = dados[3]
    ARQUIVO_SAIDA = dados[4]
    TAMNHO_ARQUIVO = dados[5]

    with open(ARQUIVO_SAIDA, "rb") as arquivo:
        IMAGEM_BYTES = arquivo.read()

    TAMANHO_ARQUIVO = len(IMAGEM_BYTES)

    SQL_LINE = '''
          INSERT INTO imagens(nome_arquivo, latitude, longitude, data_captura, tamanho_arquivo, imagem) VALUES (%s, %s, %s, %s, %s, %s)
    '''
    VALUES = (
        NOME_ARQUIVO,
        LATITUDE,
        LONGITUDE,
        DATA_CAPTURA,
        TAMANHO_ARQUIVO,
        IMAGEM_BYTES
    )

    conexao = conectar()

    try:
        cursor = conexao.cursor()
        try:
            cursor.execute(SQL_LINE, VALUES)
            conexao.commit()
            print("SALVO NO BANCO DE DADOS")
        finally:
            cursor.close()
    finally:
        conexao.close()