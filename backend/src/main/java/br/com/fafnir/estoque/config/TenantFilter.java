package br.com.fafnir.estoque.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private final TenantResolver tenantResolver;

    public TenantFilter(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UUID tenantId = tenantResolver.resolveTenantId(authentication);
            boolean isSuperAdmin = tenantResolver.resolveSuperAdmin(authentication);

            TenantContext.setTenantId(tenantId);
            TenantContext.setSuperAdmin(isSuperAdmin);

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}