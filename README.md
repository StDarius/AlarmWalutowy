# 📈 Alarm Walutowy – v1.3

Aplikacja **Alarm Walutowy** to projekt stworzony w Spring Boot, który pozwala użytkownikom zakładać subskrypcje na wybrane pary walutowe i otrzymywać powiadomienia e-mail w przypadku przekroczenia określonych progów procentowych zmiany kursu.

## ✨ Nowości w wersji 1.3
- ✅ Dodanie **DTO (Data Transfer Objects)** w warstwie API:
    - Dane zwracane do użytkownika są teraz ograniczone tylko do potrzebnych pól (np. brak haseł i informacji wewnętrznych).
    - Większe bezpieczeństwo oraz czytelniejszy JSON w odpowiedziach API.
- ✅ Refaktoryzacja kontrolerów (`AuthController`, `UserController`, `SubscriptionController`) do korzystania z DTO.
- ✅ Lepsza separacja logiki: Encje służą wyłącznie do mapowania bazy danych, a DTO do komunikacji z klientem.
- ✅ Drobne poprawki w testach oraz konfiguracji aplikacji.

---

## 🛠 Technologie
- Java 21
- Spring Boot 3.3.4
- Spring Data JPA (Hibernate)
- Spring Security (JWT)
- Flyway (migracje bazy danych)
- H2 (profil dev) / PostgreSQL (profil prod)
- MailHog (testowanie powiadomień e-mail)
- Maven

---

## 📊 Baza danych
- **Dev** – H2 (in-memory, profil `dev`)
- **Prod** – PostgreSQL (profil `prod`)
- Schemat tworzony i aktualizowany przez **Flyway**.
- Relacje:
    - `User` ↔ `Subscription` (OneToMany)
    - W pełni znormalizowana struktura (brak redundancji).

---

## 🚀 Uruchamianie
### Wymagania
- Java 21
- Maven 3.9+
- (opcjonalnie dla `prod`) PostgreSQL + RabbitMQ

### Kroki (profil `dev`)
1. Uruchom MailHog:
   ```bash
   mailhog
Interfejs: http://localhost:8025

Zbuduj projekt:

mvn clean install

Uruchom moduł dataprovider:

mvn spring-boot:run -pl dataprovider -Dspring-boot.run.profiles=dev

API dostępne pod:
http://localhost:8080/swagger-ui/index.html

🔑 Funkcjonalności
#Rejestracja i logowanie użytkowników (JWT).
#Tworzenie i usuwanie subskrypcji walutowych.
#Automatyczne sprawdzanie kursów walut i wysyłanie powiadomień e-mail.
#Bezpieczne API oparte na DTO.

🧪 Testowanie maili
Wszystkie powiadomienia e-mail są wysyłane do MailHog (port 1025).

Możesz je podejrzeć w interfejsie webowym:
http://localhost:8025

📌 Plany na wersję 1.4
Dodanie panelu frontendowego (React/Angular).

Wdrożenie aplikacji w chmurze (np. VPS/Heroku).

Dodanie cache dla wyników kursów.

Rozszerzenie testów integracyjnych o symulację kursów.

👤 Autor
Projekt stworzony jako część ścieżki rozwoju Java Developera szkoły Coderslab.
Wersja 1.3 stanowi stabilne wydanie z pełnym wsparciem DTO oraz poprawioną strukturą API.