# 💱 Alarm Walutowy – System Powiadomień o Kursach Walut

**Alarm Walutowy** to projekt oparty na **Spring Boot**, którego celem jest śledzenie kursów walut i powiadamianie użytkowników, gdy skonfigurowane progi zostaną przekroczone.  
Projekt pokazuje integrację **bazy danych, systemu wiadomości, bezpieczeństwa oraz powiadomień mailowych** w modularnej aplikacji Java.

---

## 📌 Funkcjonalności

- **Logowanie i autoryzacja JWT**
- **CRUD dla Subskrypcji** (waluta + próg procentowy)
- **Pobieranie aktualizacji kursów** (RabbitMQ lub pamięć in-memory w trybie dev)
- **Historia kursów** (zapisywanie 50 ostatnich próbek)
- **Monitorowanie progów**: uruchamianie alertów po przekroczeniu ustawionej wartości
- **Powiadomienia mailowe (v1.2)** – obsługiwane przez [MailHog](https://github.com/mailhog/MailHog) w trybie deweloperskim
- Dokumentacja API przez Swagger UI (`/swagger-ui.html`)

---

## 🗂️ Moduły

- `dataprovider` – REST API, baza danych, bezpieczeństwo, messaging, serwis powiadomień
- `datagatherer` – symuluje pobieranie kursów walut i publikowanie zdarzeń
- `common` – klasy wspólne (DTO, zdarzenia, konfiguracja)

---

## ⚙️ Technologie

- **Java 17**, **Spring Boot 3**
- **Spring Data JPA + Flyway** (H2 w dev, PostgreSQL w prod)
- **Spring Security + JWT**
- **RabbitMQ** (opcjonalnie, wyłączone w dev)
- **MailHog SMTP** (testowanie maili w dev)
- **Maven**
- **Swagger / OpenAPI**

---

## 🚀 Uruchomienie w trybie dev

1. Uruchom **MailHog** (SMTP `1025`, panel pod `8025`):

   docker run --rm -d -p 1025:1025 -p 8025:8025 mailhog/mailhog

2.Uruchom aplikację (dataprovider):

mvn spring-boot:run -pl dataprovider

3. Wejdź na:

Swagger UI → http://localhost:8080/swagger-ui.html
MailHog inbox → http://localhost:8025

📧 Testowanie maili (v1.2)
Dodano specjalny endpoint debugowy, który pozwala ręcznie wysłać przykładowy mail:

POST /api/debug/mail

Authorization: Bearer <twój_token>
Efekt: wysyłane jest testowe powiadomienie o przekroczeniu progu.
Mail pojawi się w MailHog UI.

🛠️ Profile aplikacji

dev (domyślny):

Baza H2 (in-memory)
Messaging w pamięci (bez RabbitMQ)
Powiadomienia mailowe → MailHog

prod:

PostgreSQL
RabbitMQ
Zewnętrzny serwer SMTP (konfigurowalny)

📜 Changelog
v1.0 – Szkielet aplikacji (dataprovider, datagatherer, common)

v1.1 – CRUD, JWT, Swagger UI, testy integracyjne

v1.2 – Powiadomienia mailowe z MailHog, NotificationService, endpoint debugowy do testów

🧑‍💻 Kolejne kroki
Dodanie DTO do zwracanych odpowiedzi API

Wdrożenie na VPS (Docker Compose)

Dodanie CI/CD (np. GitHub Actions)

Obsługa zewnętrznego SMTP w środowisku produkcyjnym