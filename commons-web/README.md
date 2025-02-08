# 🌐 Commons Web Library

This library provides boilerplate code for basic Spring MVC web applications, based on projects generated with Bootify ([https://bootify.io/](https://bootify.io/)) or similar structures.  It aims to streamline the development process by offering pre-built components and configurations.

## 💡 Tips & Tricks

### 🛠️ Usage

If this library is available in your company's JFrog Artifactory, simply add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ludo.mentis.aciem</groupId>
    <artifactId>commons-web</artifactId>
    <version>TYPE THE VERSION NUMBER HERE</version>
</dependency>
```
Otherwise, navigate to this directory in your terminal and run `mvn install` to compile and install it in your local Maven repository.

### ⚙️ Configuration

This library includes classes annotated with `@Component` (or derived annotations like `@Service` or `@Repository`).  To ensure Spring scans these components when using the library in your project, you'll need to configure component scanning in your Spring configuration. Here's an example:

```java
@Configuration
@ComponentScans(value = {
    @ComponentScan("ludo.mentis.aciem.tabellarius"), // Example: Scanning your package
    @ComponentScan("ludo.mentis.aciem.commons.web") // Example: Scanning this package
})
public class AppConfig {
    // ... your configuration ...
}
```

### 📄 Thymeleaf Fragments & Templates

This library relies on specific Thymeleaf fragments and templates.  Due to the complexities of sharing fragments and templates across JARs, it's recommended to examine projects that utilize this library and copy the necessary fragments/templates directly into your application.  This approach simplifies integration and ensures consistency.