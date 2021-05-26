package jwzp.security.spring_security_jwt_authenticate.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //Przypisujemy obowiązkowe uwierzytelnienie przy wywołaniu posta /books oraz uwierzytelnienie z autoryzacją przy wowałuniu Delete /books/{id}
        http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/books").authenticated()
                .antMatchers(HttpMethod.DELETE, "/books/*").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTFilter(authenticationManager()));
    }
}
