#!/bin/bash

/usr/share/tomcat9/bin/startup.sh
/opt/hbase-$HBASE_VERSION/bin/hbase regionserver start
