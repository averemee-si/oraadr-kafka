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

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import eu.solutions.a2.utils.ExceptionUtils;

public class AdrFileUnmarshallerSingleton {

	private static final Logger LOGGER = Logger.getLogger(AdrFileUnmarshallerSingleton.class);
	private static AdrFileUnmarshallerSingleton instance = null;
	private final JAXBContext jaxbContext;

	private AdrFileUnmarshallerSingleton() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(Msg.class);
	}

	public static synchronized AdrFileUnmarshallerSingleton getInstance() {
		if (instance == null) {
			try {
				instance = new AdrFileUnmarshallerSingleton();
			} catch (JAXBException e) {
				LOGGER.error(ExceptionUtils.getExceptionStackTrace(e));
			}
		}
		return instance;
	}

	public Msg getMessageAsPojo(String msgAsString) throws JAXBException {
		StringReader sr = new StringReader(msgAsString);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return ((Msg) unmarshaller.unmarshal(sr));
	}
}
