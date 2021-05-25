package jwzp.security.spring_security_jwt_authenticate.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jwzp.security.spring_security_jwt_authenticate.models.User;
import jwzp.security.spring_security_jwt_authenticate.service.JWTGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@RestController
public class JWTController {
    String secret = "841D8A6C80CBA4FCAD32D5367C18C53B";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws JOSEException {
        return JWTGenerator.generateJWT(user);
    }
}
