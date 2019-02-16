package integration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.slupicki.lideo.LideoDemoAppApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.Map;

@SpringBootTest(classes = LideoDemoAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
public class SpringIntegrationTest {

    private static final String BASE_TEMPLATE = "{protocol}://{host}:{port}{path}";

    @Autowired
    protected RestTemplate restTemplate;

    @Value("${server.port}")
    protected String serverPort;

    public <T> T get(String path, Class<T> responseType) {
        return get(path, new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return TypeToken.of(responseType).getType();
            }
        });
    }

    public <T> T get(String path, ParameterizedTypeReference<T> responseType) {
        String url = prepareUrl(BASE_TEMPLATE, Maps.newHashMap(), ImmutableMap.of("path", path));
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );
        return response.getBody();
    }

    private String prepareUrl(String service) {
        return prepareUrl(service, Maps.newHashMap(), Maps.newHashMap());
    }

    private String prepareUrl(String service, Map<String, String> queryParams, Map<String, String> uriParams) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(service);
        queryParams.forEach(uriComponentsBuilder::queryParam);
        uriComponentsBuilder.host("localhost");
        uriComponentsBuilder.port(serverPort);
        Map<String, String> fullUriParams = Maps.newHashMap(uriParams);
        fullUriParams.putIfAbsent("protocol", "http");
        UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(fullUriParams);
        return uriComponents.toUriString();
    }


}