FROM quay.io/keycloak/keycloak:26.4.2

# Copiar archivos de configuración
COPY MetaMapa.json /opt/keycloak/data/import/MetaMapa.json

# El comando start --import-realm se configurará en Railway
# como Start Command o se puede especificar aquí
CMD ["start", "--import-realm"]
