# 🛡️ Commons Security Library

This library provides common OAuth 2.0-related code for both client and resource servers. It simplifies OAuth 2.0 integration by offering reusable components.

## ⚙️ Components

### `CustomAuthenticationProvider`

A custom authentication provider that interacts with an OAuth 2.0 Authorization Server to authenticate users. It retrieves a token based on username and password, parses it, and loads authorities (roles/entitlements) into an `UsernamePasswordAuthenticationToken` object. This is useful for implementing login pages that delegate authentication to an OAuth 2.0 Authorization Server. 🔑
  
### `JwtAuthenticationFilter`

A Spring MVC filter designed for REST APIs. It intercepts requests, looks for an "Authorization" header starting with "Bearer " (indicating a JWT token), validates the token, and extracts claims like username and authorities. ⚙️

## 💡 Tips & Tricks

### 🛠️ Usage

If this library is available in your company's JFrog Artifactory, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ludo.mentis.aciem</groupId>
    <artifactId>commons-security</artifactId>
    <version>1.0.0</version>
</dependency>
```

Otherwise, navigate to this directory in your terminal and run `mvn install` to compile and install it in your local Maven repository.

### ⚙️ Configuration

This library contains classes annotated with `@Component` (or derived annotations).  Configure component scanning in your Spring configuration to ensure Spring recognizes these components. Example:

```java
@Configuration
@ComponentScans(value = {
    @ComponentScan("ludo.mentis.aciem.tabellarius"), // Example: Scanning your package
    @ComponentScan("ludo.mentis.aciem.commons.security") // Example: Scanning this library's package
})
public class AppConfig {
    // ... your configuration ...
}
```

### 🔗 OpenFeign Integration

This library uses OpenFeign to communicate with the OAuth 2.0 Authorization Server (e.g., "Auctoritas").  Add this library's package to the list of base packages scanned by OpenFeign.  Example:

```java
@Configuration
@EnableFeignClients(basePackages = {
    "ludo.mentis.aciem.tesserarius.client", // In case your project also uses OpenFeign directly
    "ludo.mentis.aciem.commons.security.client" // OpenFeign clients in this library
})
public class AppConfig {
    // ... your configuration ...
}
```

### ⚠️ Important Considerations

*   **Spring Cloud Version:** Ensure your application uses the same Spring Cloud version as this library to avoid compatibility issues.
*   **Spring Boot Version:** Use the same Spring Boot version as this library to maintain compatibility.
```
