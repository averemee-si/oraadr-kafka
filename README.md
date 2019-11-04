No longer maintained, please try [oralog](https://github.com/averemee-si/oralog) instead

# oraadr-kafka

[Oracle Database](https://www.oracle.com/database/index.html) [Automatic Diagnostic Repository](https://docs.oracle.com/en/database/oracle/oracle-database/12.2/admin/managing-diagnostic-data.html#GUID-8DEB1BE0-8FB9-4FB2-A19A-17CF6F5791C3) messages to [Apache Kafka](http://kafka.apache.org/) or [Amazon Kinesis](https://aws.amazon.com/kinesis/) transfer.

## Getting Started

These instructions will get you a copy of the project up and running on your Oracle Database Server.

### Prerequisites

Before using oraadr-kafka please check that required Java8+ is installed with

```
echo "Checking Java version"
java -version
```


### Installing

Build with

```
mvn install
```
Then run as root supplied `install.sh` or run commands below

```
ORAADR_HOME=/opt/a2/agents/oraadr

mkdir -p $ORAADR_HOME/lib
cp target/lib/*.jar $ORAADR_HOME/lib

cp target/oraadr-kafka-*.jar $ORAADR_HOME
cp oraadr-kafka.sh $ORAADR_HOME
cp oraadr-kafka.conf $ORAADR_HOME
cp log4j.properties $ORAADR_HOME

chmod +x $ORAADR_HOME/oraadr-kafka.sh
```

### Configuration for Apache Kafka

Create topic using command line interface, for example: 

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic oraadr-test
```

Don't forget about correct sizing of topic for heavy load.
If you using Amazon Managed Streaming for Apache Kafka you can use [AWS Management Console](https://console.aws.amazon.com/msk) 

Edit `oraadr-kafka.conf`, this files should looks like

```
a2.file.query.interval = 500

a2.watched.adr.file.0 = /d00/install/APPS/12.1.0/admin/EBSDB_apps/diag/rdbms/ebsdb/EBSDB/alert/log.xml
a2.watched.adr.message.prefix.0 = rdbms1
a2.watched.adr.file.1 = /d00/install/APPS/12.1.0/admin/EBSDB_apps/diag/tnslsnr/apps/ebsdb/alert/log.xml
a2.watched.adr.message.prefix.1 = tnslsnr1

a2.kafka.servers = kafka.a2-solutions.eu:9092
a2.kafka.topic = ora-audit-topic
a2.kafka.client.id = a2.audit.ai.ora112

```
#### Mandatory parameters ####
`a2.watched.adr.file[.N]` - valid file path to Oracle ADR file located under **diag_dest**

`a2.watched.adr.message.prefix[.N]` - prefix used for message key generation

`a2.file.query.interval` - interval in milliseconds between check for new messages

`a2.kafka.servers` - hostname/IP address and port of Kafka installation

`a2.kafka.topic` - value must match name of Kafka topic created on previous step

`a2.kafka.client.id` - use any valid string value for identifying this Kafka producer


#### Optional parameters ####
`a2.data.format` - when set to `RAW` (default value) sends message as raw string in Oracle ADR XML format. When set to `JSON` sends JSON formatted data with field names same as in **[V$DIAG_ALERT_EXT](https://docs.oracle.com/en/database/oracle/oracle-database/12.2/refrn/V-DIAG_ALERT_EXT.html)**/**X$DBGALERTEXT**

`a2.kafka.security.protocol` - must be set to `SSL` or `SASL_SSL` if you like to transmit files using SSL and enable auth. Only PLAIN authentication supported and tested at moment.

`a2.kafka.security.truststore.location` - set to valid certificate store file if `a2.security.protocol` set to `SSL` or `SASL_SSL`

`a2.kafka.security.truststore.password` - password for certificate store file if `a2.security.protocol` set to `SSL` or `SASL_SSL`

`a2.kafka.security.jaas.config` - JAAS login module configuration. Must be set when `a2.security.protocol` set to `SASL_SSL`. For example **org.apache.kafka.common.security.plain.PlainLoginModule required username="alice" password="alice-secret";** . Do not forget to escape equal sign and double quotes in file.

`a2.kafka.acks` - number of acknowledgments. Please check [Apache Kafka documentation](https://kafka.apache.org/documentation/#configuration) for more information about `acks` parameter
 
`a2.kafka.batch.size` - producer batch size. Please check [Apache Kafka documentation](https://kafka.apache.org/documentation/#configuration) for more information about `batch.size` parameter 

`a2.kafka.buffer.memory` - producer buffer memory. Please check [Apache Kafka documentation](https://kafka.apache.org/documentation/#configuration) for more information about `buffer.memory` parameter 

`a2.kafka.compression.type` - compression type. Please check [Apache Kafka documentation](https://kafka.apache.org/documentation/#configuration) for more information about `compression.type` parameter. By default set to `gzip`, to disable compression set to `uncompressed` 

`a2.kafka.linger.ms` - producer linger time. Please check [Apache Kafka documentation](https://kafka.apache.org/documentation/#configuration) for more information about `linger.ms` parameter

`a2.kafka.max.request.size` - maximum size of producer producer request. Please check [Apache Kafka documentation](https://kafka.apache.org/documentation/#configuration) for more information about `max.request.size` parameter

`a2.kafka.retries` - producer retries config. Please check [Apache Kafka documentation](https://kafka.apache.org/documentation/#configuration) for more information about `retries` parameter



### Configuration for Amazon Kinesis 
Create Kinesis stream using [AWS Management Console](https://console.aws.amazon.com/kinesis) or using AWS CLI, for example:

```
aws kinesis create-stream --stream-name ora-adr-test --shard-count 1
```
Check stream's creation progress using [AWS Management Console](https://console.aws.amazon.com/kinesis) or with AWS CLI, for example:

```
aws kinesis describe-stream --stream-name ora-adr-test
```
Don't forget about correct sizing of stream for heavy load.

Edit `oraadr-kafka.conf`, this files should looks like

```
a2.target.broker = kinesis

a2.file.query.interval = 500

a2.watched.adr.file.0 = /d00/install/APPS/12.1.0/admin/EBSDB_apps/diag/rdbms/ebsdb/EBSDB/alert/log.xml
a2.watched.adr.message.prefix.0 = rdbms1
a2.watched.adr.file.1 = /d00/install/APPS/12.1.0/admin/EBSDB_apps/diag/tnslsnr/apps/ebsdb/alert/log.xml
a2.watched.adr.message.prefix.1 = tnslsnr1

a2.kinesis.region = eu-west-1
a2.kinesis.stream = ora-aud-test
a2.kinesis.access.key = AAAAAAAAAABBBBBBBBBB
a2.kinesis.access.secret = AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDD

```
#### Mandatory parameters ####

`a2.target.broker` - must set to **kinesis** for working with Amazon Kinesis 

`a2.watched.adr.file[.N]` - valid file path to Oracle ADR file located under **diag_dest**

`a2.watched.adr.message.prefix[.N]` - prefix used for message key generation

`a2.file.query.interval` - interval in milliseconds between check for new messages

`a2.kinesis.region` - AWS region

`a2.kinesis.stream` - name of Kinesis stream

`a2.kinesis.access.key` - AWS access key

`a2.kinesis.access.secret` - AWS access key secret

#### Optional parameters ####

`a2.data.format` - when set to `RAW` (default value) sends message as raw string in Oracle ADR XML format. When set to `JSON` sends JSON formatted data with field names same as in **[V$DIAG_ALERT_EXT](https://docs.oracle.com/en/database/oracle/oracle-database/12.2/refrn/V-DIAG_ALERT_EXT.html)**/**X$DBGALERTEXT**. Usage of JSON formatted data simplifies further delivery using [Amazon Kinesis Data Firehose](https://aws.amazon.com/kinesis/data-firehose/) and processing in DW or data lake.

`a2.kinesis.max.connections` - can be used to control the degree of parallelism when making HTTP requests. Using a high number will cause a bunch of broken pipe errors to show up in the logs. This is due to idle connections being closed by the server. Setting this value too large may also cause request timeouts if you do not have enough bandwidth. **1** is default value


## Running 

If running with Apache Kafka check for transferred ADR messages at [Kafka](http://kafka.apache.org/)'s side with command line consumer

```
bin/kafka-console-consumer.sh --from-beginning --zookeeper localhost:2181 --topic oraadr-test
```
If running with [Amazon Kinesis](https://aws.amazon.com/kinesis/) check for transferred ADR messages with [aws kinesis get-records](https://docs.aws.amazon.com/cli/latest/reference/kinesis/get-records.html) CLI command.


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## TODO
* Kerberos support

## Version history
###0.9.0###
Initial release
###0.9.1###
 - Amazon Kinesis Client Library instead of Amazon Kinesis Producer Library and removing KPL related parameters
 - adr.xsd
 - ability to send JSON formatted data using a2.data.format option


## Authors

* **Aleksej Veremeev** - *Initial work* - [A2 Solutions](http://a2-solutions.eu/)

## License

This project is licensed under the Apache License - see the [LICENSE](LICENSE) file for details

