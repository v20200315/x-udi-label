# X-UDI-LABEL

Spring Boot application for UDI label management. The project currently provides the foundation for a product API and management console.

## Requirements

- Maven
- Java version managed by the `x-platform-parent` Maven parent

## Run locally

The default profile is `dev`, configured in `src/main/resources/application.yaml`. It runs on port `8083` and optionally imports `x-udi-label-dev.yml` from Nacos at `localhost:8848`.

Start the application:

```bash
mvn spring-boot:run
```

To run with another profile, for example `test`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

The `test` and `prod` profiles also optionally import their configuration from Nacos. Configure a reachable Nacos server if those settings are needed.

## Routes

| Route | Description |
| --- | --- |
| `GET /console` | Management console page |
| `/api/v1/products` | Product API base path; endpoints are not implemented yet |
| `/api/v1/ai/**` | Permitted by the current security configuration |
| Other `/api/**` routes | Require authentication |

The console route is currently permitted by the security configuration. The Spring Boot entry point is `com.lsxuan.udilabel.UdiLabelApplication`.

## UDI terminology

- **UDI** — Unique Device Identification（医疗器械唯一标识）
- **DI** — Device Identifier（器械标识符）
- **PI** — Production Identifier（生产标识符）

In simple terms:

- **UDI-DI** identifies a specific medical device.
- **UDI-PI** identifies production information, such as a lot number, serial number, production date, or expiration date.

UDI usually consists of UDI-DI and UDI-PI. The PI data included depends on the applicable regulations and coding rules.
