/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.core.log;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.macgyver.core.log.EventLogger.LogMessage;
import io.macgyver.neorx.rest.NeoRxClient;
import io.macgyver.test.MacGyverIntegrationTest;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.registry.Registration;
import reactor.bus.selector.Selectors;

public class EventLoggerIntegrationTest extends MacGyverIntegrationTest {

	@Autowired
	EventBus eventBus;

	@Autowired
	EventLogger eventLogger;

	@Autowired
	NeoRxClient neo4j;
	
	@Test
	public void testIt() throws InterruptedException {
	
		Assertions.assertThat(eventBus).isNotNull();
		
		CountDownLatch latch = new CountDownLatch(1);
		AtomicReference<Event<LogMessage>> ref = new AtomicReference<Event<LogMessage>>(null);
		Registration reg = eventBus.on(Selectors.T(LogMessage.class),(Event<LogMessage> x)->{
			ref.set(x);
			latch.countDown();
		});
		
		String id = UUID.randomUUID().toString();
		
		((LogMessage)eventLogger.event().withAttribute("foo", "bar").withAttribute("test", id)).log();

		Assertions.assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
		

		Assertions.assertThat(ref.get().getData().getData().path("foo").asText()).isEqualTo("bar");
	
		eventBus.getConsumerRegistry().unregister(reg);
		
		
		
	}
	
	@Test
	public void testEventBuilder() {
		
		String id = UUID.randomUUID().toString();
		
		ObjectNode n = new ObjectMapper().createObjectNode().put("test2", id);
		
		LogMessage builder = (LogMessage) eventLogger.event().withData(n);
		
		Assertions.assertThat(builder.getData().path("test2").asText()).isEqualTo(id);
		
		Assertions.assertThat(builder.getTimestamp().toEpochMilli()).isCloseTo(System.currentTimeMillis(), Offset.offset(1000L));
		
		Assertions.assertThat(builder.label).isNull();
	}
	
}
