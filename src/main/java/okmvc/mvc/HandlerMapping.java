package okmvc.mvc;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HandlerMapping {

    private Map<Request, Handler> handlerMapping;

    public void init() {

    }
}
