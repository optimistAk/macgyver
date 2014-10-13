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

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonConverter implements TreeModelConverter {

	@SuppressWarnings("unchecked")
	@Override
	public <X> X convertObject(Object input, Class<X> output) {

		try {

			if (input == null) {
				return null;
			}
			X x = (X) new ObjectMapper().reader().readTree(input.toString());
			return x;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
