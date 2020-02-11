#!/bin/bash

/usr/local/tomcat/bin/startup.sh
/opt/hbase-$HBASE_VERSION/bin/hbase regionserver start
