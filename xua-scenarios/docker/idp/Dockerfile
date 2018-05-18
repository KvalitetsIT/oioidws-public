FROM php:7.1-apache

# Utilities
RUN apt-get update && \
    apt-get -y install apt-transport-https git curl vim --no-install-recommends && \
    rm -r /var/lib/apt/lists/*

# SimpleSAMLphp
ARG SIMPLESAMLPHP_VERSION=1.15.4
RUN curl -s -L -o /tmp/simplesamlphp.tar.gz https://github.com/simplesamlphp/simplesamlphp/releases/download/v$SIMPLESAMLPHP_VERSION/simplesamlphp-$SIMPLESAMLPHP_VERSION.tar.gz && \
    tar xzf /tmp/simplesamlphp.tar.gz -C /tmp && \
    rm -f /tmp/simplesamlphp.tar.gz  && \
    mv /tmp/simplesamlphp-* /var/www/simplesamlphp && \
    touch /var/www/simplesamlphp/modules/exampleauth/enable

# Copy files Apache
COPY apache/ports.conf /etc/apache2
COPY apache/sites-available/simplesamlphp.conf /etc/apache2/sites-available

# Apache certs
COPY apache-cert/cert.crt /etc/ssl/cert/cert.crt
COPY apache-cert/private.key /etc/ssl/private/private.key

# Config Simplesamlphp
COPY var-www-simplesamlphp/ /var/www/simplesamlphp/

# Config Apache
RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf && \
    a2enmod ssl && \
    a2dissite 000-default.conf default-ssl.conf && \
    a2ensite simplesamlphp.conf


EXPOSE 80 443

# Set work dir
WORKDIR /var/www/simplesamlphp
