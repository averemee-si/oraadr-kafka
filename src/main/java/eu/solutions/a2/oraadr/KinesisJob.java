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

import org.apache.log4j.Logger;

import com.amazonaws.services.kinesis.producer.UserRecordResult;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import eu.solutions.a2.utils.ExceptionUtils;

public class KinesisJob implements Callable<Void> {

	private static final Logger LOGGER = Logger.getLogger(KinesisJob.class);

	final String prefix;
	final String message;
	final CommonJobSingleton commonData;
	final KinesisSingleton kinesisData;

	KinesisJob(final String prefix, final String message) {
		this.prefix = prefix;
		this.message = message;
		this.commonData = CommonJobSingleton.getInstance();
		this.kinesisData = KinesisSingleton.getInstance();
	}

	@Override
	public Void call() {
		final long startTime = System.currentTimeMillis();
		final String kinesisKey = prefix + ":" + startTime;
		final byte[] msgBytes = message.getBytes();
		ListenableFuture<UserRecordResult> futureResult =
					kinesisData.producer().addUserRecord(
								kinesisData.topic(), kinesisKey, ByteBuffer.wrap(msgBytes));
		Futures.addCallback(futureResult,
						new KinesisAsyncCallback(msgBytes.length, startTime));			
		return null;
	}

	private class KinesisAsyncCallback implements FutureCallback<UserRecordResult> {

		private final int msgSize;
		private final long startTime;

		KinesisAsyncCallback(final int msgSize, final long startTime) {
			this.msgSize = msgSize;
			this.startTime = startTime;
		}
		
		@Override
		public void onSuccess(UserRecordResult result) {
			commonData.addFileData(
					msgSize,
					System.currentTimeMillis() - startTime);
		}

		@Override
		public void onFailure(Throwable t) {
			LOGGER.error("Exception while sending message to Kinesis!!!" );
			LOGGER.error(ExceptionUtils.getExceptionStackTrace(new Exception(t)));
		}
	}
}
