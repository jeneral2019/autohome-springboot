FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=123456

COPY my.cnf /etc/mysql/conf.d/my.cnf

RUN mkdir -p /var/log/mysql

RUN chown -R mysql:mysql /var/log/mysql