package moe.sndy.carbon.instances;

import java.io.Serializable;
import java.util.Map;

public abstract class Adapter implements Serializable {

    private static final long serialVersionUID = 1L;

    public Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public void setAttribute(Object value, String... path) {
        Map<String, Object> nested = attributes;
        for (int i = 0; i < path.length - 1; i++) {
            nested = (Map<String, Object>) nested.get(path[i]);
        }
        nested.put(path[path.length - 1], value);
    }

    @SuppressWarnings("unchecked")
    public Object getAttribute(String... path) {
        Map<String, Object> nested = attributes;
        for (int i = 0; i < path.length - 1; i++) {
            nested = (Map<String, Object>) nested.get(path[i]);
        }
        return nested.get(path[path.length - 1]);
    }

    public long getVersion() {
        return (Long) getAttribute("version");
    }

    public abstract void adapt();

}
