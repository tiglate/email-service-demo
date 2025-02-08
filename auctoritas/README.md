# 🔑 Auctoritas: OAuth2 Authorization Server

This module implements an OAuth2 Authorization Server using Java 13 and Spring Boot 2.  It provides a basic UI for managing users, applications, and roles, along with a REST API that mirrors Spring's OAuth2 Authorization Server endpoints and data contracts as closely as possible.

A key feature of this implementation is the dynamic generation of the private/public key pair used for JWT token signing.  The pair is generated upon application startup and resides only in the server's RAM. This enhances security by preventing the key pair from being easily compromised.  It does mean, however, that the key pair changes on every restart.

## ⚠️ Important Note

This application attempts to register itself with a Netflix Eureka server.  Ensure that Eureka is up and running *before* starting Auctoritas.