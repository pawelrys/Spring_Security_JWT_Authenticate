package jwzp.security.spring_security_jwt_authenticate.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jwzp.security.spring_security_jwt_authenticate.models.User;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

public class JWTGenerator {

    static String secret = "841D8A6C80CBA4FCAD32D5367C18C53B";

    public static ResponseEntity<?> generateJWT(User user) throws JOSEException {
        JWSSigner signer = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));
        //Sprawdzenie, czy dane logowania sie zgadzają
        //Jeżeli nie:
        //return ResponseEntity.status(401).body("Incorrect login details!");

        //Zakładam, że odnalezione dane logowania należą do administratora
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .claim("login", user.getUsername())
                .claim("password", user.getPassword())
                .claim("role", "ROLE_ADMIN")
                //Ważność tokenu to 20 sekund VV
                .expirationTime(new Date(new Date().getTime() + 1020 * 1000))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(), jwtClaimsSet);
        signedJWT.sign(signer);

        return ResponseEntity.of(Optional.of(signedJWT.serialize()));
    }
}
