FROM quay.io/keycloak/keycloak:26.4.2

COPY MetaMapa.json /opt/keycloak/data/import/MetaMapa.json

CMD ["start", "--import-realm"]
