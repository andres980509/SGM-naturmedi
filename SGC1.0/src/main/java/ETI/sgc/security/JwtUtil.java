package ETI.sgc.security;

import ETI.sgc.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET =
            "CAMBIAR_ESTA_CLAVE_SUPER_LARGA_Y_SEGURA_123456";

    public static String generar(Usuario u) {

        return Jwts.builder()
                .setSubject(u.username)
                .claim("rol", u.rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24)) // 24 horas
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validar(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}