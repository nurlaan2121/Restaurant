package lms.exceptions.jwt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lms.entities.Instructor;
import lms.entities.Student;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;

/**
 * @author Mukhammed Asantegin
 */
@Service
public class JwtService {
    private final String secretKey = "java12";
    // create jwt token  // encode token
    public String createTokenForStud(Student student) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        return JWT.create()
                .withClaim("email", student.getUsername())
                .withClaim("id", student.getId())
                .withClaim("name", student.getName())
                .withClaim("role", student.getRole().name())
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withExpiresAt(ZonedDateTime.now().plusHours(1).toInstant())
                .sign(algorithm);
    }
    public String createTokenForInst(Instructor instructor) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        return JWT.create()
                .withClaim("email", instructor.getUsername())
                .withClaim("id", instructor.getId())
                .withClaim("name", instructor.getName())
                .withClaim("role", instructor.getRole().name())
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withExpiresAt(ZonedDateTime.now().plusHours(1).toInstant())
                .sign(algorithm);
    }


    // verify token     // decode token
    public String verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String email = decodedJWT.getClaim("email").asString();
        return email;

    }
}