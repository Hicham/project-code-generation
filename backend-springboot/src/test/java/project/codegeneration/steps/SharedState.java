package project.codegeneration.steps;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;

@Component
public class SharedState {
    private HttpHeaders headers = new HttpHeaders();

    private ResponseEntity<String> response;
    public ResponseEntity<String> getResponse() {
        return response;
    }

    public void setResponse(ResponseEntity<String> response) {
        this.response = response;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }
    
}
