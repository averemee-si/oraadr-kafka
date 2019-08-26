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

package eu.solutions.a2.oraadr.schema;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import eu.solutions.a2.utils.ExceptionUtils;

public class AdrFileKafkaSerializer implements Serializer<Msg> {

	private static final Logger LOGGER = Logger.getLogger(AdrFileKafkaSerializer.class);
	private static final ObjectWriter writer = new ObjectMapper()
			.setSerializationInclusion(Include.NON_NULL)
			.writer();

	@Override
	public byte[] serialize(String topic, Msg data) {
		try {
			return writer.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			LOGGER.error(ExceptionUtils.getExceptionStackTrace(e));
		}
		return null;
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub		
	}

}
