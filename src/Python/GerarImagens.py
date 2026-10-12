
from PIL import Image
import os
import random
from collections import defaultdict
from dotenv import load_dotenv
from DividirImagens import cortarImagens
from ListaLocalizacoes import coordenadas_cerrado
from datetime import datetime, timedelta
from sentinelhub import (
    SHConfig,
    BBox,
    CRS,
    DataCollection,
    SentinelHubRequest,
    SentinelHubCatalog,
    MimeType
)



# 1. Configurações
load_dotenv()

#importar o módulo para dividir imagens

config = SHConfig()
config.sh_client_id = os.getenv("COPERNICUS_CLIENT_ID")
config.sh_client_secret = os.getenv("COPERNICUS_CLIENT_SECRET")
config.sh_base_url = "https://sh.dataspace.copernicus.eu"
config.sh_token_url = (
    "https://identity.dataspace.copernicus.eu/"
    "auth/realms/CDSE/protocol/openid-connect/token"
)

INICIO_TEMPO = "2020-01-01"
FIM_TEMPO = "2025-12-31"
QUANTIDADE_IMAGENS = 1
TAMANHO = (2500, 2500)

latitude, longitude = random.choice(coordenadas_cerrado)

bbox = BBox(
    bbox=(
        longitude - 2,
        latitude - 2,
        longitude,
        latitude
    ),
    crs=CRS.WGS84
)

# Pasta das imagens originais
PASTA_SAIDA = os.path.join(os.getcwd(), "../../Imagens", "Originais")
os.makedirs(PASTA_SAIDA, exist_ok = True)

print("Inicialização concluída.")




# 2. Busca datas disponíveis

def buscarDatas(bbox, quantidade):
    catalog = SentinelHubCatalog(config=config)

    resultados = list(
        catalog.search(
            DataCollection.SENTINEL2_L2A,
            bbox = bbox,
            time = (INICIO_TEMPO, FIM_TEMPO)
        )
    )

    if not resultados:
        return []

    # Agrupa a cobertura de nuvens por data
    nuvens_por_data = defaultdict(list)

    for item in resultados:
        propriedades = item.get("properties", {})
        dataHora = propriedades.get("datetime")

        if not dataHora:
            continue

        data = dataHora[:10]

        nuvens = propriedades.get("eo:cloud_cover", 100)
        nuvens_por_data[data].append(float(nuvens))

    # Calcula a média de cobertura de nuvens
    candidatas = []
    for data, valores in nuvens_por_data.items():
        media_nuvens = sum(valores) / len(valores)
        candidatas.append((data, media_nuvens))


    # Menor cobertura média primeiro
    candidatas.sort(key=lambda item: item[1])

    print("Imagem encontrada.")

    return candidatas[:quantidade]




# 3. Evalscript RGB

evalscript = """
//VERSION=3

function setup() {
    return {
        input: [{
            bands: ["B02", "B03", "B04", "dataMask"]
        }],
        output: {
            bands: 4,
            sampleType: "UINT8"
        }
    };
}

function evaluatePixel(sample) {
    return [
        Math.min(255, sample.B04 * 2.5 * 255),
        Math.min(255, sample.B03 * 2.5 * 255),
        Math.min(255, sample.B02 * 2.5 * 255),
        sample.dataMask * 255
    ];
}
"""

# 4. Pesquisa as datas

datasSelecionadas = buscarDatas(
    bbox,
    QUANTIDADE_IMAGENS
)

if not datasSelecionadas:
    raise SystemExit(
        "Nenhuma cena encontrada para o período informado."
    )

print(f"Datas selecionadas: {len(datasSelecionadas)}")


# 5. Gera uma imagem por data

for contador, (dataImagem, nuvensMedia) in enumerate(datasSelecionadas, start = 1):
    data_central = datetime.strptime(dataImagem, "%Y-%m-%d")
    data_inicio = (data_central - timedelta(days=15)).strftime("%Y-%m-%d")
    data_fim = (data_central + timedelta(days=15)).strftime("%Y-%m-%d")
    caminho = os.path.join(PASTA_SAIDA, f"imagem_{dataImagem}.png")

    dadosImagem = [dataImagem, latitude, longitude, f"imagem_{dataImagem}.png"]

    print("-----------------------------")
    print(f"Imagem: {contador}")
    print(f"Data: {dataImagem}")
    print(f"Cobertura média das cenas: {nuvensMedia:.2f}%")
    print(f"Latitude e Longitude: {latitude}, {longitude}")
    print("-----------------------------")

    # Evita baixar novamente um arquivo já existente
    if os.path.exists(caminho):
        print(f"Arquivo já existe: {caminho}")
        continue


    # Monta a requisição para o dia escolhido
    request = SentinelHubRequest(
        evalscript=evalscript,
        input_data=[
            SentinelHubRequest.input_data(
                data_collection=DataCollection.SENTINEL2_L2A.define_from(
                    "s2l2a",
                    service_url=config.sh_base_url
                ),
                time_interval=(
                    data_inicio + "T00:00:00Z",
                    data_fim + "T23:59:59Z"
                ),
                mosaicking_order="leastCC"
            )
        ],
        responses=[SentinelHubRequest.output_response("default", MimeType.PNG)],
        bbox=bbox,
        size=TAMANHO,
        config=config
    )

    try:

        print("Baixando imagem...")

        dados = request.get_data()

        if not dados:
            print("Nenhum dado retornado nesta data.")
            continue

        imagem = dados[0]

        Image.fromarray(imagem).save(caminho)

        print(f"Imagem salva: {caminho}")
        print(f"Dimensões: {imagem.shape[1]} x {imagem.shape[0]} pixels")

        print("Cortando imagem...")
        cortarImagens(caminho, contador, dadosImagem)

    except Exception as erro:

        print(f"Falha ao gerar a imagem de {dataImagem}:")
        print(erro)

        continue

print()
print("Processamento finalizado.")
