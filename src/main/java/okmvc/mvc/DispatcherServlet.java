package okmvc.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import okmvc.core.Container;
import okmvc.exception.InitializingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {

    private static Container container = new Container();

    static {
        try {
            container.init();
        } catch (ClassNotFoundException e) {
            throw new InitializingException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String method = request.getMethod().toUpperCase();
        String url = request.getPathInfo();
        Request req = new Request(method, url);
        Handler handler = container.getHandlerMapping().get(req);
        if (handler == null) {
            throw new RuntimeException("cannot find request mapping for url: " + url);
        }
        Class<?> clazz = handler.getControllerClass();
        Method m = handler.getMethod();
        Object controller = container.getControllerMap().get(clazz);

        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, Object> param = new HashMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            if (value == null || value.length == 0) continue;
            param.put(name, value[0]);  // TODO
        }
        Object result;
        try {
            result = m.invoke(controller, param);
        } catch (Exception e) {
            throw new RuntimeException("cannot invoke method: " + m.getName() + " on controller: " + clazz.getSimpleName());
        }
        if (result instanceof JsonData) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(((JsonData) result).data));
            writer.flush();
            writer.close();
        } else if (result instanceof Map) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString((result)));
            writer.flush();
            writer.close();
        }

    }
}