package okmvc.mvc;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private String method;
    private String url;
}
