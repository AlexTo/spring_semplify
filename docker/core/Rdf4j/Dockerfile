FROM amd64/tomcat:8.5-jre8-alpine
MAINTAINER Bart Hanssens (bart.hanssens@bosa.fgov.be)

ARG VERSION="3.0.4"

ENV CATALINA_OPTS="-Dorg.eclipse.rdf4j.appdata.basedir=/var/rdf4j"

RUN adduser -S tomcat

RUN cd /tmp && \
	wget -q "http://mirror.internode.on.net/pub/eclipse/rdf4j/eclipse-rdf4j-${VERSION}-sdk.zip" -O /tmp/rdf4j.zip && \
	unzip -q /tmp/rdf4j.zip && \
	rm -rf /usr/local/tomcat/webapps/* && \
	cp /tmp/eclipse-rdf4j-${VERSION}/war/*.war /usr/local/tomcat/webapps && \
	rm -rf /tmp/eclipse && \
	rm /tmp/rdf4j.zip && \
	mkdir -p /var/rdf4j && \
	chown -R tomcat /var/rdf4j /usr/local/tomcat && \
	chmod a+x /usr/local/tomcat /usr/local/tomcat/bin /usr/local/tomcat/bin/catalina.sh

COPY web.xml /usr/local/tomcat/conf/web.xml

USER tomcat

VOLUME /var/rdf4j
VOLUME /usr/local/tomcat/logs

EXPOSE 8080