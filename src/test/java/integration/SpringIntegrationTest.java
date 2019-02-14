package integration;

import com.google.common.collect.Maps;
import com.slupicki.lideo.LideoDemoAppApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(classes = LideoDemoAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
public class SpringIntegrationTest {

    @Autowired
    protected RestTemplate restTemplate;

    public <T> T get(String path, Class<T> responseType) {
        return restTemplate.getForObject("http://localhost:8080" + path, responseType, Maps.newHashMap());
    }

    public <T> List<T> getList(String path, ParameterizedTypeReference<List<T>> responseType) {
        ResponseEntity<List<T>> response = restTemplate.exchange(
                "http://localhost:8080" + path,
                HttpMethod.GET,
                null,
                responseType
        );
        return response.getBody();
    }
}