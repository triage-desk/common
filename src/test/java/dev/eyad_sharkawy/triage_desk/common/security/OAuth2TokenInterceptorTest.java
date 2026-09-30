package dev.eyad_sharkawy.triage_desk.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class OAuth2TokenInterceptorTest {

    private final OAuth2TokenInterceptor interceptor = new OAuth2TokenInterceptor();
    private HttpRequest request;
    private ClientHttpRequestExecution execution;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpRequest.class);
        execution = mock(ClientHttpRequestExecution.class);
        headers = new HttpHeaders();
        ClientHttpResponse response = mock(ClientHttpResponse.class);

        when(request.getHeaders()).thenReturn(headers);
        when(execution.execute(request, new byte[0])).thenReturn(response);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAddBearerTokenWhenJwtIsPresent() throws IOException {
        Jwt jwt =
                new Jwt(
                        "mock-token-xyz",
                        Instant.now(),
                        Instant.now().plusSeconds(3600),
                        Map.of("alg", "none"),
                        Map.of("sub", "user-123"));
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        interceptor.intercept(request, new byte[0], execution);

        assertThat(headers.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer mock-token-xyz");
        verify(execution).execute(request, new byte[0]);
    }

    @Test
    void shouldNotAddBearerTokenWhenUnauthenticated() throws IOException {
        interceptor.intercept(request, new byte[0], execution);

        assertThat(headers.getFirst(HttpHeaders.AUTHORIZATION)).isNull();
        verify(execution).execute(request, new byte[0]);
    }

    @Test
    void shouldNotAddBearerTokenWhenCredentialsNotJwt() throws IOException {
        org.springframework.security.core.Authentication auth =
                mock(org.springframework.security.core.Authentication.class);
        when(auth.getCredentials()).thenReturn("not-a-jwt");
        SecurityContextHolder.getContext().setAuthentication(auth);

        interceptor.intercept(request, new byte[0], execution);

        assertThat(headers.getFirst(HttpHeaders.AUTHORIZATION)).isNull();
        verify(execution).execute(request, new byte[0]);
    }
}
