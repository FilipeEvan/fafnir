package br.com.fafnir.estoque.config;

import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<UUID> currentTenant = new ThreadLocal<>();

    private static final ThreadLocal<Boolean> superAdmin = ThreadLocal.withInitial(() -> false);

    public TenantContext() {
    }

    public static void setTenantId(UUID tenantId) {
        currentTenant.set(tenantId);
    }

    public static UUID getTenantId() {
        return currentTenant.get();
    }

    public static void setSuperAdmin(boolean isSuperAdmin) {
        superAdmin.set(isSuperAdmin);
    }

    public static boolean isSuperAdmin() {
        return Boolean.TRUE.equals(superAdmin.get());
    }

    public static void validateTenantAccess(UUID resourceTenantId) {
        UUID currentTenantId = getTenantId();

        if (!isSuperAdmin() && (currentTenantId == null || !currentTenantId.equals(resourceTenantId))) {
            throw new AccessDeniedException("Apenas usuários desta empresa e administradores podem acessar este recurso.");
        }
    }

    public static void clear() {
        currentTenant.remove();
        superAdmin.remove();
    }
}
