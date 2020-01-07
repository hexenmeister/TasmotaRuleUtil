package de.as.ustils.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class JsonData {

    public static class JsonObject extends JsonData {
        private Map<String, JsonData> data = new LinkedHashMap<>();

        @Override
        public boolean isObject() {
            return true;
        }

        @Override
        public Map<String, JsonData> getEntries() {
            return this.data;
        }

        public JsonData get(String key) {
            return this.data.get(key);
        }

        @Override
        public String toString() {
            return this.data.entrySet().stream().map(v -> "\"" + v.getKey() + "\"" + ":" + v.getValue().toString())
                    .collect(Collectors.joining(",", "{", "}"));
        }
    }

    public static class JsonValue extends JsonData {
        public static enum Type {
            STRING, BOOLEAN, NUMBER, NULL;
        }

        private Object dataObj;
        private Type type;

        public JsonValue(Type type, Object data) {
            this.dataObj = data;
            this.type = type;
        }

        @Override
        public boolean isValue() {
            return true;
        }

        @Override
        public Object getValue() {
            return this.dataObj;
        }

        @Override
        public String toString() {
            if (this.type == Type.STRING) {
                return "\"" + this.dataObj + "\"";
            }
            return this.dataObj.toString();
        }
    }

    public static class JsonArray extends JsonData {
        private List<JsonData> values = new ArrayList<>();

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public List<JsonData> getElements() {
            return this.values;
        }

        public JsonData get(int index) {
            return this.values.get(index);
        }

        @Override
        public String toString() {
            return this.values.stream().map(x -> x.toString()).collect(Collectors.joining(",", "[", "]"));
        }
    }
    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isValue() {
        return false;
    }

    public Map<String, JsonData> getEntries() {
        return null;
    }

    public List<JsonData> getElements() {
        return null;
    }

    public Object getValue() {
        return null;
    }

    public JsonData getPath(String path) {
        // e.g. get("Status.FriendlyName.1");
        if (this.isValue()) {
            return null;
        }
        String[] ps = path.split("\\.", 2);
        if (ps.length < 1) {
            return null;
        }
        JsonData node = null;
        if (this.isObject()) {
            node = ((JsonObject) this).get(ps[0]);
        } else if (this.isArray()) {
            node = ((JsonArray) this).get(Integer.parseInt(ps[0]));
        }
        if (node == null) {
            return null;
        }
        if (ps.length > 1) {
            return node.getPath(ps[1]);
        }
        return node;
    }
}
