package management.subscription.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableMethodSecurity
public class EntityAuthentication {

     @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterchain(HttpSecurity http)throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user = User.builder()
                .username("vishnu")
                .password(passwordEncoder().encode("vish"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("vishnuvardhan")
                .password(passwordEncoder().encode("vishnu"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user,admin);
    }

}
