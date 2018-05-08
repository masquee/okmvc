package okmvc.core;


import lombok.Getter;
import lombok.Setter;
import okmvc.annotation.*;
import okmvc.exception.InitializingException;
import okmvc.exception.ReflectionException;
import okmvc.mvc.Handler;
import okmvc.mvc.Request;
import okmvc.util.ClassUtils;
import okmvc.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class Container {

    private static final Logger LOG = LoggerFactory.getLogger(Container.class);

    private List<Class<?>> controllerClassList = new ArrayList<>();
    private List<Class<?>> serviceClassList = new ArrayList<>();
    private List<Class<?>> allClass = new ArrayList<>();

    private Map<Class<?>, Object> serviceMap = new HashMap<>();
    private Map<Class<?>, Object> controllerMap = new HashMap<>();

    private Map<Request, Handler> handlerMapping = new HashMap<>();

    public void init() throws ClassNotFoundException {
        String packageName = PropertyUtils.getRootPackageName();
        List<String> classNames = ClassUtils.listClassNames(packageName);
        ClassLoader classLoader = ClassUtils.getClassLoader();
        for (String name : classNames) {
            Class<?> clazz = classLoader.loadClass(name);
            allClass.add(clazz);
            if (clazz.getAnnotation(Controller.class) != null) {
                controllerClassList.add(clazz);
                controllerMap.put(clazz, ClassUtils.newInstance(clazz));
            } else if (clazz.getAnnotation(Service.class) != null) {
                serviceClassList.add(clazz);
                serviceMap.put(clazz, ClassUtils.newInstance(clazz));
            }
        }
        inject();
        initHandlerMapping();
    }

    // TODO 注入功能目前只支持向Controller中注入Service，待扩展
    private void inject() {
        for (Map.Entry<Class<?>, Object> entry : controllerMap.entrySet()) {
            Class<?> clazz = entry.getKey();
            Object instance = entry.getValue();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Autowired.class) == null) continue;
                Class<?> fieldType = field.getType();
                Object fieldValue = serviceMap.get(fieldType);
                if (fieldValue == null) {
                    throw new InitializingException("cannot autowired property: " + field.getName());
                }
                field.setAccessible(true);
                try {
                    field.set(instance, fieldValue);
                } catch (IllegalAccessException e) {
                    throw new ReflectionException("cannot set property: " + field.getName());
                }
                LOG.debug("inject property [{}] of bean [{}] successful", field.getName(), clazz.getSimpleName());
            }
        }
    }

    public void initHandlerMapping() {
        for (Class<?> clazz : controllerClassList) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getAnnotation(GetMapping.class) != null) {
                    GetMapping annotation = method.getAnnotation(GetMapping.class);
                    String url = annotation.value();
                    Request request = new Request("GET", url);
                    Handler handler = new Handler(clazz, method);
                    handlerMapping.put(request, handler);
                } else if (method.getAnnotation(PostMapping.class) != null) {
                    PostMapping annotation = method.getAnnotation(PostMapping.class);
                    String url = annotation.value();
                    Request request = new Request("POST", url);
                    Handler handler = new Handler(clazz, method);
                    handlerMapping.put(request, handler);
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}
