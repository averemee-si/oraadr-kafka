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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import eu.solutions.a2.utils.ExceptionUtils;

public class OraadrKafka {

	private static final Logger LOGGER = Logger.getLogger(OraadrKafka.class);

	/** Default interval in milliseconds between file tail query */
	private static final int FILE_QUERY_INTERVAL = 1000;
	/** Prefix for watched file parameter(s) */
	private static final String WATCHED_FILE_PREFIX = "a2.watched.adr.file";

	private static final Properties props = new Properties();
	/** Supported target systems */
	private static final int TARGET_KAFKA = 0;
	private static final int TARGET_KINESIS = 1;
	/** Set default target system to Apache Kafka */
	private static int targetSystem = TARGET_KAFKA;
	/** Supported data formats */
	protected static final int DATA_FORMAT_RAW_STRING = 0;
	protected static final int DATA_FORMAT_JSON = 1;
	/**   Main thread pool for data tansfer jobs */
	private static ThreadPoolExecutor threadPool;

	public static void main(String[] argv) {

		initLog4j(1);
		if (argv.length == 0) {
			printUsage(OraadrKafka.class.getCanonicalName(), 2);
		}
		loadProps(argv[0], 1);

		String targetBroker = props.getProperty("a2.target.broker", "kafka").trim();
		if ("kafka".equalsIgnoreCase(targetBroker)) {
			targetSystem = TARGET_KAFKA;
		} else if ("kinesis".equalsIgnoreCase(targetBroker)) {
			targetSystem = TARGET_KINESIS;
		} else {
			LOGGER.fatal("wrong target broker type specified in configuration file " + argv[0]);
			LOGGER.fatal("Exiting.");
			System.exit(2);
		}

		int fileQueryInterval = FILE_QUERY_INTERVAL;
		final String fileQueryIntervalString = props.getProperty("a2.file.query.interval");
		if (fileQueryIntervalString != null && !"".equals(fileQueryIntervalString)) {
			try {
				fileQueryInterval = Integer.parseInt(fileQueryIntervalString);
			} catch (Exception e) {
				LOGGER.warn("Incorrect value for a2.file.query.interval -> " + fileQueryIntervalString);
				LOGGER.warn("Setting it to " + FILE_QUERY_INTERVAL);
			}
		}
		// For use in Lambda
		final int fileQueryIntervalFinal = fileQueryInterval;

		// Set default data format to Raw string
		int dataFormat = DATA_FORMAT_RAW_STRING;
		final String dataFormatString = props.getProperty("a2.data.format");
		if (dataFormatString != null && !"".equals(dataFormatString)) {
			if ("RAW".equalsIgnoreCase(dataFormatString)) {
				dataFormat = DATA_FORMAT_RAW_STRING;
			} else if ("JSON".equalsIgnoreCase(dataFormatString)) {
				dataFormat = DATA_FORMAT_JSON;
			} else {
				LOGGER.warn("Incorrect value for a2.data.format -> " + dataFormatString);
				LOGGER.warn("Setting it to RAW");
			}
		}

		// Init CommonJob MBean
		CommonJobSingleton.getInstance(dataFormat);

		String osName = System.getProperty("os.name").toUpperCase();
		LOGGER.info("Running on " + osName);
		if (targetSystem == TARGET_KAFKA) {
			KafkaSingleton.getInstance().parseSettings(props, argv[0], dataFormat, 6);
		} else if (targetSystem == TARGET_KINESIS) {
			KinesisSingleton.getInstance().parseSettings(props, argv[0], 6);
		}

		List<AbstractMap.SimpleImmutableEntry<Thread, Tailer>> adrTailers = new ArrayList<>();
		props.forEach((key, value) -> {
			final String paramName = ((String)key).toLowerCase();
			if (paramName.startsWith(WATCHED_FILE_PREFIX)) {
				final String filePath = (String) value;
				File watchedFile = null;
				try {
					watchedFile = new File(filePath);
				} catch (Exception fnf) {
					LOGGER.fatal(paramName + " points to nonexisting file " + filePath);
					LOGGER.fatal("Exiting.");
					System.exit(3);
				}
				// Sanity check - must be file
				if (!watchedFile.isFile()) {
					LOGGER.fatal(filePath + " must be file.");
					LOGGER.fatal("Exiting.");
					System.exit(3);
				}
				// Corresponding message prefix
				final String msgPrefixParam = "a2.watched.adr.message.prefix" + 
											paramName.substring(WATCHED_FILE_PREFIX.length());
				final String msgPrefix = (String) props.getOrDefault(msgPrefixParam, "adr-" + adrTailers.size());
				// Create listener and tailer
				AdrTailListener atl = new AdrTailListener(msgPrefix);
				Tailer tailer = new Tailer(watchedFile, atl, fileQueryIntervalFinal, true);
				LOGGER.info("Watching for " + filePath);
				LOGGER.info("Using prefix " + msgPrefix);
				// Corresponding thread
				Thread thread = new Thread(tailer);
				thread.setDaemon(true);
				thread.setName("Thread-" + msgPrefix);

				adrTailers.add(new AbstractMap.SimpleImmutableEntry<Thread, Tailer>(
								thread, tailer));
			}
		});

		// Add special shutdown thread
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				adrTailers.forEach((entry) -> {
					// Stop tailer
					entry.getValue().stop();
				});
				if (targetSystem == TARGET_KAFKA) {
					KafkaSingleton.getInstance().shutdown();
				} else if (targetSystem == TARGET_KINESIS) {
					KinesisSingleton.getInstance().shutdown();
				}
				LOGGER.info("Shutting down...");
				//TODO - more information about processing
			}
		});

		// Start watching
		adrTailers.forEach((entry) -> {
			Thread thread = entry.getKey();
			LOGGER.info("Starting thread " + thread.getName());
			thread.start();
		});
		while (true) {
			try {
				Thread.sleep(fileQueryInterval * 16);
			} catch (Exception e) {}
		}
	}

	private static class AdrTailListener extends TailerListenerAdapter {

		final private String prefix;

		private StringBuffer msg = null;
		private boolean msgFlag = false;
		private long msgCount = 0;

		AdrTailListener(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public void handle(final String line) {
			final String trimmedLine = line.trim();
			if (trimmedLine.startsWith("<msg")) {
				msgFlag = true;
				msg = new StringBuffer(1024);
				msg.append(line);
				msg.append("\n");
			} else if (trimmedLine.endsWith("</msg>")) {
				if (msgFlag) {
					msgFlag = false;
					msg.append(line);
					Callable<Void> processJob = null;
					if (targetSystem == TARGET_KAFKA) {
						processJob = new KafkaJob(prefix, msgCount++, msg.toString());
					} else if (targetSystem == TARGET_KINESIS) {
						processJob = new KinesisJob(prefix, msgCount++, msg.toString());
					}
					try {
						threadPool.submit(processJob);
					} catch (RejectedExecutionException ree) {
						LOGGER.error("Can't send message to Kafka!");
						LOGGER.error(ExceptionUtils.getExceptionStackTrace(ree));
					}
				} else {
					LOGGER.error("Message ends without start tag!");
					LOGGER.error("Bad data = " + line);
				}
			} else if (msgFlag) {
				msg.append(line);
				msg.append("\n");
			} else {
				LOGGER.error("Unrecognized Oracle ADR file xml format!");
				LOGGER.error("Bad data = " + line);
			}
		}
	}

	private static void initLog4j(int exitCode) {
		// Check for valid log4j configuration
		String log4jConfig = System.getProperty("a2.log4j.configuration");
		if (log4jConfig == null || "".equals(log4jConfig)) {
			System.err.println("JVM argument -Da2.log4j.configuration must set!");
			System.err.println("Exiting.");
			System.exit(exitCode);
		}

		// Check that log4j configuration file exist
		Path path = Paths.get(log4jConfig);
		if (!Files.exists(path) || Files.isDirectory(path)) {
			System.err.println("JVM argument -Da2.log4j.configuration points to unknown file " + log4jConfig + "!");
			System.err.println("Exiting.");
			System.exit(exitCode);
		}
		// Initialize log4j
		PropertyConfigurator.configure(log4jConfig);

	}

	private static void printUsage(String className, int exitCode) {
		LOGGER.fatal("Usage:\njava " + className + " <full path to configuration file>");
		LOGGER.fatal("Exiting.");
		System.exit(exitCode);
	}

	private static void loadProps(String configPath, int exitCode) {
		try {
			props.load(new FileInputStream(configPath));
		} catch (IOException eoe) {
			LOGGER.fatal("Unable to open configuration file " + configPath);
			LOGGER.fatal(ExceptionUtils.getExceptionStackTrace(eoe));
			LOGGER.fatal("Exiting.");
			System.exit(exitCode);
		}
	}

}
