Here is the Markdown (.md) file content for the full JWT-based Spring Security with Role-Based Access example, formatted for documentation or GitHub usage:


---

# 🛡️ Spring Security - JWT Authentication with Role-Based Access

This guide provides a complete, runnable Spring Boot example demonstrating:

- ✅ Stateless JWT authentication
- ✅ Role-based access using `@PreAuthorize`
- ✅ Custom filter and security configuration

---

## 📁 Project Structure

src/main/java ├── config │   └── SecurityConfig.java ├── controller │   └── TestController.java ├── filter │   └── JwtAuthFilter.java ├── model │   └── AuthRequest.java ├── service │   └── JwtService.java └── AuthController.java

---

## 🧱 Dependencies (`pom.xml`)

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>


---

🔐 SecurityConfig.java

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/auth/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN").build();
        UserDetails user = User.withUsername("user").password(passwordEncoder().encode("user123")).roles("USER").build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


---

🔑 JwtService.java

@Service
public class JwtService {
    private static final String SECRET = "secretkey123456789012345678901234";

    public String generateToken(UserDetails user) {
        return Jwts.builder()
            .setSubject(user.getUsername())
            .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET.getBytes()).build()
            .parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET.getBytes()).build()
            .parseClaimsJws(token).getBody();
        return claims.get("roles", List.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername());
    }
}


---

🚨 JwtAuthFilter.java

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired private JwtService jwtService;
    @Autowired private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}


---

🔐 AuthController.java

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;
    @Autowired private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (auth.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}


---

📦 AuthRequest.java

public class AuthRequest {
    private String username;
    private String password;
    // Getters and setters
}


---

✅ TestController.java (Role-Based Endpoints)

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Only Admins can access this";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userEndpoint() {
        return "Only Users can access this";
    }

    @GetMapping("/common")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String commonEndpoint() {
        return "Accessible by both Admin and User";
    }
}


---

🧪 How to Test

1. Get JWT Token

POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response:

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

2. Access Secured Endpoint

GET /api/admin
Authorization: Bearer <token>


---

✅ Features Summary

Feature	Status

JWT Stateless Auth	✅
Role-Based Access with @PreAuthorize	✅
Secure Login Endpoint	✅
Token Validation Filter	✅
In-Memory Users with Roles	✅



---

🧠 Interview Talking Points

How JWT is generated and validated

Role extraction from claims

Use of SecurityContextHolder

Stateless session management

Difference between @PreAuthorize vs @Secured

Why CSRF is disabled in JWT-based APIs


---

Would you like this converted into a downloadable `.md` file or GitHub README, or should I bundle it with a working Maven project zip?

