# Custom Serialization Framework

## Overview

This project implements a high-performance custom serialization framework in Java. Unlike Java's built-in serialization, this framework allows for custom serialization protocols, optional compression, encryption, and versioning. It is designed to handle complex Java objects, including cyclic dependencies, with optimized performance and security.

## Features

- **Custom Serialization Protocol**: Design and use your own serialization format.
- **Compression**: Supports GZIP compression to reduce serialized data size.
- **Encryption**: Supports AES encryption to secure serialized data.
- **Versioning**: Handles changes to object structures over time, ensuring backward compatibility.
- **Cyclic Dependencies**: Efficiently manages cyclic references between objects.

## Prerequisites

- Java 8 or higher
- Maven (for building and managing dependencies)

## Project Structure

- `com.custom.serialization`: Contains the core serialization framework, including `Serializer`, `Deserializer`, `CustomSerializable`, and related classes.
- `com.custom.serialization.compress`: Includes the `Compressor` interface and the `GZIPCompressor` implementation.
- `com.custom.serialization.encrypt`: Includes the `Encryptor` interface and the `AESEncryptor` implementation.
- `com.custom.serialization.test`: Contains test classes and examples demonstrating the use of the framework.

