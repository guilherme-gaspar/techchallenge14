package com.fiap.techchallenge14.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private static final String USERS_ENDPOINT = "/v1/users";
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/v1/login", "/v3/api-docs", "/swagger-ui",
            "/webjars", "/swagger-resources"
    );

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicEndpoint(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(AUTH_HEADER);
        if (header == null || !header.startsWith(PREFIX)) {
            sendUnauthorized(response, request);
            return;
        }

        String token = header.substring(PREFIX.length());

        if (!jwtService.isValid(token)) {
            sendUnauthorized(response, request);
            return;
        }

        String username = jwtService.extractUsername(token);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path, String method) {
        if (path.equals(USERS_ENDPOINT) && method.equalsIgnoreCase(HttpMethod.POST.name()))
            return true;

        return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith)
                && (method.equalsIgnoreCase(HttpMethod.POST.name())
                || method.equalsIgnoreCase(HttpMethod.GET.name()));
    }

    private void sendUnauthorized(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Acesso negado");
        problem.setType(URI.create("/problems/unauthorized"));
        problem.setDetail("Token JWT ausente ou inválido. Envie no header 'Authorization: Bearer <token>'");
        problem.setInstance(URI.create(request.getRequestURI()));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/problem+json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), problem);
    }
}
