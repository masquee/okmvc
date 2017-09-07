package okmvc.mvc;

import lombok.*;

import java.lang.reflect.Method;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Handler {
    private Class<?> controllerClass;
    private Method method;
}
