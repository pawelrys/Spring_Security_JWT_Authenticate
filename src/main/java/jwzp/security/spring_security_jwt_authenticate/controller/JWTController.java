package jwzp.security.spring_security_jwt_authenticate.controller;

import com.nimbusds.jose.JOSEException;
import jwzp.security.spring_security_jwt_authenticate.models.User;
import jwzp.security.spring_security_jwt_authenticate.service.JWTGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws JOSEException {
        return JWTGenerator.generateJWT(user);
    }
}
