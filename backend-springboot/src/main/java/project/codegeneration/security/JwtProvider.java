package project.codegeneration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.services.MyUserDetailsServices;

import java.security.Key;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtProvider {

    private JwtKeyProvider keyProvider;
    private MyUserDetailsServices userDetailsService;

    public JwtProvider(JwtKeyProvider keyProvider, MyUserDetailsServices userDetailsService) {
        this.keyProvider = keyProvider;
        this.userDetailsService = userDetailsService;

    }


    public String createToken(User user) {
        Key privateKey = keyProvider.getPrivateKey();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        String token = Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiration)
                .claim("auth", user.getRoles().stream().map(Role::toString).toList())
                .claim("userId", user.getId())
                .signWith(privateKey)
                .compact();


        return token;
    }

    public Authentication getAuthentication(String token) {
        PublicKey publicKey = keyProvider.getPublicKey();

        Claims claims = Jwts.parser().verifyWith(publicKey)
                .build().parseSignedClaims(token).getPayload();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


}
