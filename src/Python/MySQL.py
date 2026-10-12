import mysql.connector

def conectar():
    return mysql.connector.connect(
        host = "localhost",
        user = "root",
        password = "",
        database = "aps_26_2"
    )


def salvarBanco(dados):
    conexao = conectar()
    cursor = conexao.cursor()

    print("SALVANDO NO BANCO")

    DATA_CAPTURA = dados[0]
    LATITUDE = dados[1]
    LONGITUDE = dados[2]
    NOME_ARQUIVO = dados[3]
    ARQUIVO_SAIDA = dados[4]
    TAMNHO_ARQUIVO = dados[5]

    SQL_LINE = '''
          INSERT INTO imagens(nome_arquivo, latitude, longitude, data_captura, tamanho_arquivo, imagem) VALUES (%s, %s, %s, %s, %s, %s)
    '''
    VALUES = (
        NOME_ARQUIVO,
        LATITUDE,
        LONGITUDE,
        DATA_CAPTURA,
        TAMNHO_ARQUIVO,
        ARQUIVO_SAIDA
    )


    cursor.execute(SQL_LINE, VALUES)

    conexao.commit()
    cursor.close()
    conexao.close()

    print("SALVO NO BANCO DE DADOS")