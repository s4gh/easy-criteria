package easycriteria.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstancesInits implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final InstancesInits DEFAULT = new InstancesInits();

    public static final InstancesInits DIRECT  = new InstancesInits("*");

    public static final InstancesInits DIRECT2  = new InstancesInits("*.*");

    private final boolean initAllProps;

    private final InstancesInits defaultValue;

    private final Map<String,InstancesInits> propertyToInits = new HashMap<String,InstancesInits>();

    public InstancesInits(String... initStrs) {
    	
    	System.out.println("InstancesInits constructor invoked with " + Arrays.asList(initStrs));
    	
        boolean initAllProps = false;
        InstancesInits defaultValue = DEFAULT;

        Map<String, Collection<String>> properties = new HashMap<>();
        for (String initStr : initStrs) {
            if (initStr.equals("*")) {
                initAllProps = true;
            } else if (initStr.startsWith("*.")) {
                initAllProps = true;
                defaultValue = new InstancesInits(initStr.substring(2));
            } else {
                String key = initStr;
                List<String> inits = Collections.emptyList();
                if (initStr.contains(".")) {
                    key = initStr.substring(0, initStr.indexOf('.'));
                    inits = Arrays.asList(initStr.substring(key.length() + 1));
                }
                Collection<String> values = properties.get(key);
                if (values == null) {
                    values = new ArrayList<String>();
                    properties.put(key, values);
                }
                values.addAll(inits);
            }
        }

        for (Map.Entry<String, Collection<String>> entry : properties.entrySet()) {
        	InstancesInits inits = new InstancesInits(entry.getValue().toArray(new String[] {}));
            propertyToInits.put(entry.getKey(), inits);
        }

        this.initAllProps = initAllProps;
        this.defaultValue = defaultValue;
    }

    public InstancesInits get(String property) {
        if (propertyToInits.containsKey(property)) {
            return propertyToInits.get(property);
        } else if (initAllProps) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException(property + " is not initialized");
        }
    }

    public boolean isInitialized(String property) {
        return initAllProps || propertyToInits.containsKey(property);
    }
}
