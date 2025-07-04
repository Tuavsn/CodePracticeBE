// package com.codepractice.auth_service.config;

// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.stereotype.Component;

// import com.codepractice.auth_service.client.UserClient;
// import com.codepractice.auth_service.client.UserPresentation;

// import lombok.AllArgsConstructor;

// @Component
// @AllArgsConstructor
// public class CustomAuthenticationProvider implements AuthenticationProvider {
//     private final UserClient userClient;

//     @Override
//     public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//         String email = authentication.getName();
//         String password = authentication.getCredentials().toString();

//         UserPresentation user = userClient.getUserByEmail(email);

//         if (user == null) {
//             throw new BadCredentialsException("Invalid username or password");
//         }

//         return new UsernamePasswordAuthenticationToken(email, password);
//     }

//     @Override
//     public boolean supports(Class<?> authentication) {
//         return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//     }
// }
