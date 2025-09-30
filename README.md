# README – AlarmWalutowy v1.4.1 🎯

## 📌 Opis projektu

**AlarmWalutowy** to aplikacja służąca do monitorowania kursów walut i powiadamiania użytkowników, gdy zmiana przekroczy zdefiniowany próg procentowy.  
System umożliwia:
- zakładanie kont użytkowników i logowanie (JWT),
- subskrypcję wybranych par walutowych,
- wysyłkę powiadomień e-mail (MailHog w trybie dev),
- zarządzanie dostępem przez role (`ROLE_USER`, `ROLE_ADMIN`).

### 🆕 Zmiany w wersji 1.4.1
- Uporządkowano encje (`User`, `Role`, `RateTick`) i DTO (`RateTickDTO`, `UserDTO`, `SubscriptionDTO`).
- Poprawiono **AuthService** i **AuthController** (czystsza obsługa JWT + logowanie).
- Dodano logger (`@Slf4j`) w `NotificationService` i pełną obsługę MailHog.
- Stabilniejsze mapowanie obiektów dzięki klasie `Mappers`.
- README zaktualizowane o pełne instrukcje uruchamiania i integracji z MailHog.

---

## 🛠 Technologie

- **Java 21**, **Spring Boot 3.3**
- **Spring Security + JWT**
- **Spring Data JPA**
    - H2 (dev/test)
    - PostgreSQL (prod)
- **Flyway** – migracje bazy
- **MailHog** – testowe powiadomienia e-mail
- **Lombok** – redukcja boilerplate
- **Docker** (opcjonalnie, np. dla MailHog, bazy)
- **JUnit 5 + Mockito** – testy jednostkowe/integracyjne

---

## 🚀 Funkcjonalności

- Rejestracja i logowanie (JWT, walidacja danych).
- Subskrypcja wybranych walut z progami alertów.
- Powiadomienia e-mail (MailHog w dev).
- Integracja z zewnętrznym API kursów walut.
- Obsługa ról użytkowników:
    - `ROLE_USER` – dostęp do standardowych funkcji aplikacji.
    - `ROLE_ADMIN` – dostęp administracyjny (np. zarządzanie użytkownikami w przyszłych wersjach).

---

## 📊 Priorytety funkcjonalności (MoSCoW) + Estymaty

| Kategoria | Funkcjonalność                            | Estymata (h) | Status v1.4.1 |
|-----------|--------------------------------------------|--------------|---------------|
| M         | Rejestracja i logowanie z JWT              | 8            | ✅ Gotowe     |
| M         | Subskrypcje walut z progami alertów        | 12           | ✅ Gotowe     |
| M         | Powiadomienia e-mail (MailHog w dev)       | 6            | ✅ Gotowe     |
| M         | Integracja z zewnętrznym API (kursy walut) | 10           | ✅ Gotowe     |
| M         | Role użytkowników (USER/ADMIN)             | 8            | ✅ Gotowe     |
| S         | Historia kursów walut + DTO                | 8            | ✅ Gotowe     |
| S         | Panel statusu API (`/api/status`)          | 3            | ✅ Gotowe     |
| C         | Dashboard w React/Thymeleaf                | 16           | ❌ Jeszcze nie|
| W         | Integracja z Google OAuth                  | -            | ❌ Nie w tej wersji |
| W         | Deploy na VPS z CI/CD                      | -            | ❌ Nie w tej wersji |

---

## 📦 Uruchamianie

### Profil deweloperski

**Wymagane:**
- Java 21
- Maven
- Docker (dla MailHog i bazy testowej, opcjonalnie)

1. Uruchom MailHog:
   ```bash
   docker run --rm -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
SMTP: localhost:1025

UI MailHog: 👉 http://localhost:8025

Skonfiguruj aplikację (application.yml):

Skopiuj kod
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


Uruchom moduł dataprovider:

mvn spring-boot:run -pl dataprovider
Aplikacja dostępna pod adresem:
👉 http://localhost:8080

Profil produkcyjny
Wymagane:

PostgreSQL

RabbitMQ (dla kolejek zdarzeń walutowych)

Konfiguracja w application.yml (prod profile).
Budowanie:

Skopiuj kod
mvn clean package -DskipTests
🔑 API – przykłady
Rejestracja
POST /api/auth/register


{
"username": "jan",
"password": "tajne123",
"email": "jan@example.com"
}
Logowanie
POST /api/auth/login


{
"username": "jan",
"password": "tajne123"
}
Odpowiedź:


{
"token": "<jwt-token>",
"user": { "id": 1, "username": "jan", "email": "jan@example.com" }
}

JWT należy przekazywać w nagłówku:

makefile

Authorization: Bearer <jwt-token>
🧪 Testy
Uruchamianie testów:

mvn test

Testy jednostkowe (AuthService, UserService, NotificationService)
Testy integracyjne (API rejestracji i logowania)

📄 Wersja
Aktualne wydanie: 1.4.1

👨‍💻 Autor
Projekt stworzony w ramach bootcampu Java CodersLab
Autor: Gabriel Stremecki