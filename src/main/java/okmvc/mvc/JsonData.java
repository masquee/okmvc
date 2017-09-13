package okmvc.mvc;

import java.util.HashMap;
import java.util.Map;

public class JsonData {
    
    Map<String, Object> data = new HashMap<>();

    public void addAttribute(String name, Object value) {
        data.put(name, value);
    }
}
