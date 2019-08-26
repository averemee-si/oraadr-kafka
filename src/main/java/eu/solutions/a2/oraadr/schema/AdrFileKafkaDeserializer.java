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

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import eu.solutions.a2.utils.ExceptionUtils;

public class AdrFileKafkaDeserializer implements Deserializer<Msg> {

	private static final Logger LOGGER = Logger.getLogger(AdrFileKafkaDeserializer.class);
	private static final ObjectReader reader = new ObjectMapper().readerFor(Msg.class);

	@Override
	public Msg deserialize(String topic, byte[] data) {
		try {
			return reader.readValue(data);
		} catch (IOException e) {
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
