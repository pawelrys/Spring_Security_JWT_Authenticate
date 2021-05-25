package jwzp.security.spring_security_jwt_authenticate.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class JWTFilter extends BasicAuthenticationFilter {

    String secret = "841D8A6C80CBA4FCAD32D5367C18C53B";

    public JWTFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;

        String jwt;

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
            SignedJWT signedJWT = null;
            try {
                signedJWT = SignedJWT.parse(jwt);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            if (isSignatureValid(jwt) && signedJWT != null) {
                String username = (String) signedJWT.getPayload().toJSONObject().get("login");
                String password = (String) signedJWT.getPayload().toJSONObject().get("password");
                String role = (String) signedJWT.getPayload().toJSONObject().get("role");

                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role));
                usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(username, password, simpleGrantedAuthorities);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
        }
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request, response);
    }

    public boolean isSignatureValid(String token) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());
            if (signedJWT.verify(verifier)) {
                Date referenceTime = new Date();
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                Date expirationTime = claims.getExpirationTime();
                boolean expired = expirationTime == null || expirationTime.before(referenceTime);
                return !expired;
            }
        } catch (JOSEException | ParseException e) {
            return false;
        }
        return false;
    }
}
