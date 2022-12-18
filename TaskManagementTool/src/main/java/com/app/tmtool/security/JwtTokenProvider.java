package com.app.tmtool.security;

import com.app.tmtool.entity.Users;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        Users users = (Users) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime()+SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(users.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", Long.toString(users.getId()));
        claims.put("username", users.getUsername());
        claims.put("fullName", users.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException ex) {
            System.out.println("Invalid JWT Signature");
        }
        catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT Token");
        }
        catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT Token");
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Jwt claims string is empty");
        }
        return false;
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");
        return Long.parseLong(id);
    }
}
