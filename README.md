# AlarmWalutowy v1.5 🎯

## 📌 Opis

**AlarmWalutowy** monitoruje kursy walut i wysyła powiadomienia e‑mail, gdy zmiana przekroczy zdefiniowany próg. System składa się z dwóch mikroserwisów:

- **dataprovider** – API dla użytkowników (rejestracja/logowanie JWT, subskrypcje, odczyt danych, wysyłka maili).
- **datagatherer** – cyklicznie pobiera kursy i publikuje zdarzenia do RabbitMQ.

> **Role**: `ROLE_USER`, `ROLE_ADMIN` (seedowane przez Flyway).  
> **Powiadomienia**: w dev korzystamy z MailHog.

---

## 🆕 Co nowego w 1.5

- **Flyway**: migracje dla `roles`, `user_roles` + seed ról oraz przypięcie `ROLE_USER` do istniejących kont.
- **Docker**: multi‑stage Dockerfile, start jako **bootowalne JAR‑y** (Spring Boot 3).
- Spójna konfiguracja profilu **prod** (Docker Compose) i **dev** (lokalnie).
- **Swagger** w **dataprovider**.
- Porządki w encjach/DTO (`User`, `Role`, `RateTick`, `UserDTO`, `SubscriptionDTO`, `RateTickDTO`), uproszczony **AuthService/AuthController**, logowanie w `NotificationService`, mapowania w `Mappers`.

---

## 🧱 Architektura

```
datagatherer (8081) ──publikuje──▶ RabbitMQ ◀──subskrybuje── dataprovider (8080)
                                       │
                                PostgreSQL (Flyway)
                                       │
                                   MailHog (SMTP)
```

---

## 🛠 Technologie

Java 21, Spring Boot 3.3 • Spring Security (JWT) • Spring Data JPA • PostgreSQL • Flyway • RabbitMQ • MailHog • Docker/Compose • Lombok • JUnit 5/Mockito

---

## 🚀 Uruchamianie

### 1) Cały stos przez Docker Compose (rekomendowane)

W katalogu głównym repo:

```bash
docker compose up -d --build
```

**Kontenery i porty:**

| Nazwa             | Port hosta           | Opis                                 |
|-------------------|----------------------:|--------------------------------------|
| `ca_dataprovider` | **8080**             | API (profil `prod`)                  |
| `ca_datagatherer` | **8081**             | Zbieranie kursów (profil `prod`)     |
| `ca_postgres`     | 5432                 | Postgres + Flyway                    |
| `ca_rabbit`       | 5672 / **15672**     | RabbitMQ (AMQP / panel)              |
| `ca_mailhog`      | 1025 / **8025**      | SMTP / UI                            |

**Dostępy:**

- Swagger (dataprovider): <http://localhost:8080/swagger-ui/index.html>  
- RabbitMQ UI: <http://localhost:15672> (guest/guest)  
- MailHog UI: <http://localhost:8025>

> Compose uruchamia profile **`prod`** i spina serwisy z Postgres/RabbitMQ/MailHog. **Flyway** automatycznie tworzy schemat i seeduje role.

### 2) Lokalny dev (sam `dataprovider`)

Opcjonalnie odpal MailHog:

```bash
docker run --rm -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

Uruchom API:

```bash
mvn spring-boot:run -pl dataprovider -Dspring-boot.run.profiles=dev
```

Fragment `application-dev.yml` (dataprovider):

```yaml
spring:
  mail:
    host: localhost
    port: 1025
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false

app:
  notifications:
    enabled: true
    from: no-reply@alarm-walutowy.local
```

---

## 🗃️ Migracje (Flyway)

- Pliki: `dataprovider/src/main/resources/db/migration`
- Tabele: `users`, `subscriptions`, `rate_history`, `roles`, `user_roles`
- Seed: `ROLE_USER`, `ROLE_ADMIN` + przypięcie `ROLE_USER` do istniejących `users`

Przydatne polecenia:

```bash
# historia Flyway
docker exec -it ca_postgres psql -U postgres -d currency_alert \
  -c "select version, description, success from flyway_schema_history order by installed_rank;"

# role
docker exec -it ca_postgres psql -U postgres -d currency_alert -c "select * from roles;"
```

---

## 📚 Swagger & autoryzacja

- UI: `/swagger-ui/index.html` (tylko `dataprovider`)
- JWT w nagłówku:

```
Authorization: Bearer <jwt-token>
```

---

## 🔑 Przykłady API

**Rejestracja** – `POST /api/auth/register`

```json
{ "username": "jan", "password": "tajne123", "email": "jan@example.com" }
```

**Logowanie** – `POST /api/auth/login`

```json
{ "username": "jan", "password": "tajne123" }
```

**Odpowiedź:**

```json
{
  "token": "<jwt-token>",
  "user": { "id": 1, "username": "jan", "email": "jan@example.com" }
}
```

**Subskrypcje:**  
`GET /api/subscriptions`, `POST /api/subscriptions`, `DELETE /api/subscriptions/{id}`

---

## 📊 Priorytety (MoSCoW) – release 1.5

| Kategoria | Funkcjonalność                           | Estymata (h) | Status w 1.5 |
|-----------|-------------------------------------------|--------------|--------------|
| **M**     | Rejestracja i logowanie z JWT             | 8            | ✅           |
| **M**     | Subskrypcje walut z progami alertów       | 12           | ✅           |
| **M**     | Powiadomienia e-mail (MailHog w dev)      | 6            | ✅           |
| **M**     | Integracja z zewn. API kursów             | 10           | ✅           |
| **M**     | Role użytkowników + migracje (Flyway)     | 8            | ✅           |
| **S**     | Historia kursów + DTO                     | 8            | ✅           |
| **S**     | Panel statusu API (`/api/status`)         | 3            | ✅           |
| **C**     | Dashboard web (React/Thymeleaf)           | 16           | ❌           |
| **W**     | Google OAuth                              | -            | ❌           |
| **W**     | Deploy na VPS z CI/CD                     | -            | ❌           |

---

## 🧪 Testy

```bash
mvn test
```

- Jednostkowe: `AuthService`, `UserService`, `NotificationService`
- Integracyjne: flow rejestracja/logowanie

---

## 🧰 Build ręczny (poza Dockerem)

```bash
mvn -B -DskipTests -pl dataprovider -am clean package
mvn -B -DskipTests -pl datagatherer -am clean package
```

Uruchamianie:

```bash
java -jar dataprovider/target/dataprovider-*.jar --spring.profiles.active=prod
java -jar datagatherer/target/datagatherer-*.jar --spring.profiles.active=prod
```

---

## 📄 Wersja

Aktualne wydanie: **1.5**

---

## 👨‍💻 Autor

Projekt w ramach bootcampu Java CodersLab  
Autor: **Gabriel Stremecki**
