package restaurant.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import restaurant.entities.User;

import java.time.ZonedDateTime;
@Service
public class JwtService {
    private final String secretKey = "java12";
    public String createTokenForStud(User user) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        return JWT.create()
                .withClaim("email", user.getUsername())
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withExpiresAt(ZonedDateTime.now().plusHours(1).toInstant())
                .sign(algorithm);
    }



    public String verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String email = decodedJWT.getClaim("email").asString();
        return email;

    }
}