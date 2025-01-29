package com.zerobase.store_reservation.type;


public enum UserRole {
    PARTNER(Authority.PARTNER),  // 사용자 권한
    USER(Authority.USER);  // 관리자 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String PARTNER = "ROLE_PARTNER";
        public static final String USER = "ROLE_USER";
    }
}
