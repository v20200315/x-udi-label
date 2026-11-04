package com.lsxuan.udilabel.application.user;

public record ExternalIdentityInfo(String issuer, String subject, String email, String displayName,
                                   boolean emailVerified) {
}