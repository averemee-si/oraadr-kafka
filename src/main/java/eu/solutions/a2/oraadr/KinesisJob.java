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

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import eu.solutions.a2.oraadr.schema.AdrFileUnmarshallerSingleton;
import eu.solutions.a2.oraadr.schema.Msg;
import eu.solutions.a2.utils.ExceptionUtils;

public class KinesisJob implements Callable<Void> {

	private static final Logger LOGGER = Logger.getLogger(KinesisJob.class);

	private static final ObjectWriter writer = new ObjectMapper()
			.setSerializationInclusion(Include.ALWAYS)
			.writer();

	final String prefix;
	final String message;
	final long msgNum;
	final CommonJobSingleton commonData;
	final KinesisSingleton kinesisData;

	KinesisJob(final String prefix, final long msgNum, final String message) {
		this.prefix = prefix;
		this.message = message;
		this.msgNum = msgNum;
		this.commonData = CommonJobSingleton.getInstance();
		this.kinesisData = KinesisSingleton.getInstance();
	}

	@Override
	public Void call() {
		final long startTime = System.currentTimeMillis();
		// Add message number to prefix
		final StringBuilder kinesisKey = new StringBuilder(32);
		kinesisKey.append(prefix);
		kinesisKey.append(":");
		kinesisKey.append(msgNum);
		kinesisKey.append(":");
		kinesisKey.append(startTime);

		byte[] msgBytes = null;

		PutRecordRequest putRecordRequest  = new PutRecordRequest();
		putRecordRequest.setStreamName(kinesisData.topic());
		putRecordRequest.setPartitionKey(kinesisKey.toString());
		if (commonData.getDataFormat() == OraadrKafka.DATA_FORMAT_RAW_STRING) {
			msgBytes = message.getBytes();
		} else if (commonData.getDataFormat() == OraadrKafka.DATA_FORMAT_JSON) {
			AdrFileUnmarshallerSingleton afu = AdrFileUnmarshallerSingleton.getInstance();
			try {
				Msg msg = afu.getMessageAsPojo(message);
				msg.setAdrFileKey(prefix);
				msgBytes = writer.writeValueAsBytes(msg);
			} catch (JAXBException | JsonProcessingException e) {
				LOGGER.error("Exception while transforming message.\n" );
				LOGGER.error("Message is:\n" + message);
				LOGGER.error("ErrorStack:\n");
				LOGGER.error(ExceptionUtils.getExceptionStackTrace(e));
			}
		}
		if (msgBytes == null) {
			return null;
		}
		putRecordRequest.setData(ByteBuffer.wrap(msgBytes));
		try {
			kinesisData.producer().putRecord(putRecordRequest);
			commonData.addFileData(
					msgBytes.length,
					System.currentTimeMillis() - startTime);
		} catch (Exception e) {
			LOGGER.error("Exception while sending message to Kinesis." );
			LOGGER.error(ExceptionUtils.getExceptionStackTrace(e));
		}
		return null;
	}

}
