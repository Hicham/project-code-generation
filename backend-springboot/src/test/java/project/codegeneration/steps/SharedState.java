package project.codegeneration.steps;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;

@Component
public class SharedState {
    private HttpHeaders headers;

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }
}
