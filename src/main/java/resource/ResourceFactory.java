package resource;

import java.util.HashMap;
import java.util.Map;

import mechanics.PhysMapJson;

/**
 * Created by uschsh on 22.11.15.
 */
public final class ResourceFactory {
    private static ResourceFactory s_instance;
    private static Map<String, Resource> s_resourceMap = new HashMap<>();
    private ResourceFactory() {}

    public static ResourceFactory getInstance() {
        if (s_instance == null) {
            s_instance = new ResourceFactory();
        }
        return s_instance;
    }

    public Resource getPhysMap(String filename) {
        if(s_resourceMap.get(filename) == null)
        {
            s_resourceMap.put(filename, new PhysMapJson(filename));
        }
        return s_resourceMap.get(filename);
    }
}
