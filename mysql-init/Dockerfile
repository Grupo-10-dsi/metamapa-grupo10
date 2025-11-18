FROM mysql:8.0

# Script para crear las bases de datos
COPY init-databases.sh /init-databases.sh
RUN chmod +x /init-databases.sh

CMD ["/init-databases.sh"]
