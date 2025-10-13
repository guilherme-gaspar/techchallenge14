package com.fiap.techchallenge14.security;

import com.fiap.techchallenge14.login.storage.TokenStorage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String USERS_ENDPOINT = "/v1/users";
    private static final String LOGIN_ENDPOINT = "/v1/login";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicEndpoint(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(AUTH_HEADER);

        if (token == null || !TokenStorage.isTokenValid(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token invalido ou nao fornecido.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path, String method) {
        return path.equals(LOGIN_ENDPOINT) ||
                (path.equals(USERS_ENDPOINT) && method.equalsIgnoreCase(HttpMethod.POST.name()));
    }
}
