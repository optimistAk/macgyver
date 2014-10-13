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
package io.macgyver.xson.impl;

import io.macgyver.xson.Xson;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;

public abstract class AbstractXsonPathTest {

	Object object;

	@Before
	public void loadData() throws IOException {
		loadSampleData();
	}

	public abstract void loadSampleData() throws IOException;

	@Test
	public void testNoMatch() {
		Assert.assertNull(Xson.eval(object, "$.idx"));
		Assert.assertNull(Xson.eval(object, "$.idx[3]"));
	}

	@Test
	public void testInt() {
		Assert.assertEquals(100, Xson.eval(object, "$.id"));
	}

	@Test
	public void testString() {
		Assert.assertEquals("donut", Xson.eval(object, "$.type"));
	}

	@Test
	public void testBoolean() {
		Assert.assertEquals(true, Xson.eval(object, "$.available"));
	}

	@Test
	public void testDouble() {
		Assert.assertEquals(new Double(0.55), Xson.eval(object, "$.ppu"));
	}

	@Test
	public void testArray() {
		Assert.assertNotNull(object);

		Object x = Xson.eval(object, "$.batters.batter");
		Assert.assertNotNull(x);

		ArrayNode n = Xson.convert(x, ArrayNode.class);
		Assert.assertTrue(4 == n.size());

	}
	/*
	 * { "id": 100, "type": "donut", "name": "Cake", "available": true, "ppu":
	 * 0.55, "batters": { "batter": [ { "id": "1001", "type": "Regular" }, {
	 * "id": "1002", "type": "Chocolate" }, { "id": "1003", "type": "Blueberry"
	 * }, { "id": "1004", "type": "Devil's Food" } ] }, "toppings": ["Glazed",
	 * "Sugar", "Chocolate", "Maple"] }
	 */
}
