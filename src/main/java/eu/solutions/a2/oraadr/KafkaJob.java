/**
 * Copyright (c) 2018-present, http://a2-solutions.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package eu.solutions.a2.oraadr;

import java.util.concurrent.Callable;

import javax.xml.bind.JAXBException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import eu.solutions.a2.oraadr.schema.AdrFileUnmarshallerSingleton;
import eu.solutions.a2.oraadr.schema.Msg;
import eu.solutions.a2.utils.ExceptionUtils;

public class KafkaJob implements Callable<Void> {

	private static final Logger LOGGER = Logger.getLogger(KafkaJob.class);

	final String prefix;
	final String message;
	final long msgNum;
	final CommonJobSingleton commonData;
	final KafkaSingleton kafkaData;

	KafkaJob(final String prefix, final long msgNum, final String message) {
		this.prefix = prefix;
		this.message = message;
		this.msgNum = msgNum;
		this.commonData = CommonJobSingleton.getInstance();
		this.kafkaData = KafkaSingleton.getInstance();
	}

	@Override
	public Void call() {
		final long startTime = System.currentTimeMillis();
		// Add message number to prefix
		final StringBuilder kafkaKey = new StringBuilder(32);
		kafkaKey.append(prefix);
		kafkaKey.append(":");
		kafkaKey.append(msgNum);
		kafkaKey.append(":");
		kafkaKey.append(startTime);

		ProducerRecord<String, Object> record = null;
		if (commonData.getDataFormat() == OraadrKafka.DATA_FORMAT_RAW_STRING) {
			record = new ProducerRecord<>(kafkaData.topic(), kafkaKey.toString(), message);
		} else if (commonData.getDataFormat() == OraadrKafka.DATA_FORMAT_JSON) {
			AdrFileUnmarshallerSingleton afu = AdrFileUnmarshallerSingleton.getInstance();
			try {
				Msg msg = afu.getMessageAsPojo(message);
				msg.setAdrFileKey(prefix);
				record = new ProducerRecord<>(kafkaData.topic(), kafkaKey.toString(), msg);
			} catch (JAXBException e) {
				LOGGER.error("Exception while transforming message.\n" );
				LOGGER.error("Message is:\n" + message);
				LOGGER.error("ErrorStack:\n");
				LOGGER.error(ExceptionUtils.getExceptionStackTrace(e));
			}
		}
		if (record == null) {
			return null;
		}
		kafkaData.producer().send(
				record,
				(metadata, exception) -> {
					if (metadata == null) {
						// Error occured
						LOGGER.error("Exception while sending message to Kafka." );
						LOGGER.error(ExceptionUtils.getExceptionStackTrace(exception));
					} else {
						commonData.addFileData(
								metadata.serializedValueSize() + metadata.serializedKeySize(),
								System.currentTimeMillis() - startTime);
					}
				});
		return null;
	}

}
