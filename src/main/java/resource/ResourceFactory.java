package resource;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.vfs2.VFS;
import mechanics.PhysMapJson;

/**
 * Created by uschsh on 22.11.15.
 */
public final class ResourceFactory {
    private static ResourceFactory instance;
    private static Map<String, Resource> resourceMap = new HashMap();
    private ResourceFactory() {}

    public static ResourceFactory getInstance() {
        if (instance == null) {
            instance = new ResourceFactory();
        }
        return instance;
    }

    public Resource getPhysMap(String filename) {
        if(resourceMap.get(filename) == null)
        {
            resourceMap.put(filename, new PhysMapJson(filename));
        }
        return resourceMap.get(filename);
    }
}
