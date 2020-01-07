package de.as.ustils.json;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.InternalException;

public class JsonParser {

    public static JsonData parseJson(String jsstr) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsstr));
        JsonValue js = jsonReader.readValue();
        jsonReader.close();
        return mapJson(null, js, null);
    }

    private static JsonData mapJson(String key, JsonValue js, JsonData parent) {
        JsonData newNode = null;

        // ValueType: known types: ARRAY, OBJECT, STRING, NUMBER, TRUE, FALSE, NULL
        ValueType type = js.getValueType();
        switch (type) {
        case OBJECT:
            JsonObject jso = js.asJsonObject();
            JsonData.JsonObject mjo = new JsonData.JsonObject();
            newNode = mjo;
            jso.forEach((k, v) -> mapJson(k, v, mjo));
            break;

        case ARRAY:
            JsonArray jsa = js.asJsonArray();
            JsonData.JsonArray mja = new JsonData.JsonArray();
            newNode = mja;
            jsa.forEach((v) -> mapJson(null, v, mja));
            break;

        case STRING:
            newNode = new JsonData.JsonValue(JsonData.JsonValue.Type.STRING, ((JsonString) js).getString());
            break;

        case NUMBER:
            newNode = new JsonData.JsonValue(JsonData.JsonValue.Type.NUMBER, ((JsonNumber) js).bigDecimalValue());
            break;

        case TRUE:
            newNode = new JsonData.JsonValue(JsonData.JsonValue.Type.BOOLEAN, Boolean.TRUE);
            break;

        case FALSE:
            newNode = new JsonData.JsonValue(JsonData.JsonValue.Type.BOOLEAN, Boolean.FALSE);
            break;

        case NULL:
            newNode = new JsonData.JsonValue(JsonData.JsonValue.Type.NULL, null);
            break;

        default:
            // error. unknown type
            throw new InternalException("unknown json object type: " + type);
        }

        if (parent != null) {
            if (parent.isArray()) {
                parent.getElements().add(newNode);
            } else if (parent.isObject()) {
                parent.getEntries().put(key, newNode);
            } else if (parent.isValue()) {
                // NOP
            } else {
                // Internal error. unknown type.
                throw new InternalException("undefined mjson object type");

            }
            return parent;
        }
        return newNode;
    }

}
