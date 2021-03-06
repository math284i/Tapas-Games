/*******************************************************************************
 * Copyright (c) 2017 Michele Loreti and the jSpace Developers (see the included 
 * authors file).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package JspaceFiles.jspace.io.json;

import java.lang.reflect.Type;

import JspaceFiles.jspace.Tuple;

import JspaceFiles.gson.JsonArray;
import JspaceFiles.gson.JsonDeserializationContext;
import JspaceFiles.gson.JsonDeserializer;
import JspaceFiles.gson.JsonElement;
import JspaceFiles.gson.JsonParseException;

/**
 * This class is used to deserialize an {@link Tuple} from a {@link JsonElement}
 * (see {@link JsonDeserializer}).
 */
public class TupleDeserializer implements JsonDeserializer<Tuple> {

	@Override
	public Tuple deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (!json.isJsonArray()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		JsonArray jsa = (JsonArray) json;
		Object[] data = new Object[jsa.size()];
		jSonUtils util = jSonUtils.getInstance();
		for (int i = 0; i < jsa.size(); i++) {
			data[i] = util.objectFromJson(jsa.get(i), context);
		}
		return new Tuple(data);
	}

}
