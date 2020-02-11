FROM tonhudung/hbase-base:1.0.0-hbase-2.2.3

COPY run.sh /run.sh
RUN chmod +x /run.sh

ENV HALYARD_VERSION 3.2
ENV HALYARD_SDK_URL https://github.com/Merck/Halyard/releases/download/r$HALYARD_VERSION/halyard-sdk-$HALYARD_VERSION.zip
ENV HALYARD_WEBAPPS_URL https://github.com/Merck/Halyard/releases/download/r$HALYARD_VERSION/halyard-webapps-$HALYARD_VERSION.zip

RUN set -x \
    && mkdir /opt/halyard_sdk \
    && curl -fSL "$HALYARD_SDK_URL" -o /opt/halyard_sdk/halyard_sdk.zip \
    && unzip /opt/halyard_sdk/halyard_sdk.zip -d /opt/halyard_sdk/ \
    && rm /opt/halyard_sdk/halyard_sdk.zip

RUN apt-get install -y tomcat9

ADD setenv.sh /usr/share/tomcat9/bin

RUN set -x \
    && curl -fSL "$HALYARD_WEBAPPS_URL" -o /var/lib/tomcat9/webapps/halyard_webapps.zip \
    && unzip /var/lib/tomcat9/webapps/halyard_webapps.zip -d /var/lib/tomcat9/webapps/halyard_webapps.zip \
    && rm /var/lib/tomcat9/webapps/halyard_webapps.zip

EXPOSE 16020 16030 8080

CMD ["/run.sh"]
