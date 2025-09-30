# Alarm Walutowy — v1.4.1

Aplikacja mikroserwisowa do monitorowania kursów walut i wysyłania powiadomień użytkownikom, gdy przekroczone zostaną zdefiniowane progi.  
Projekt zrealizowany w ramach **finałowego projektu bootcampu Java (CodersLab)**.

---

## ✨ Funkcjonalności

- **Zarządzanie użytkownikami**
    - Rejestracja z adresem e-mail i hasłem
    - Role systemowe (`ROLE_USER`)
    - Uwierzytelnianie oparte o JWT

- **Subskrypcje**
    - Użytkownik może zapisać się na pary walut (np. EUR/USD)
    - Definiowanie progów procentowych zmian, które uruchamiają powiadomienia

- **Powiadomienia**
    - E-maile wysyłane przy przekroczeniu progów
    - Możliwość włączenia/wyłączenia powiadomień w konfiguracji

- **Zbieranie danych**
    - Pobieranie kursów walut z zewnętrznego dostawcy
    - Publikacja zdarzeń kursowych (RateTick)

- **Dostawca danych**
    - REST API dla użytkowników, uwierzytelniania, subskrypcji i alertów
    - Integracja z Spring Security, JPA i Mail
    - Mapowanie encji na DTO

---

## 🏗️ Struktura projektu

Projekt jest **wielomodułowy (Maven multi-module)**:

- **currency-alert** – moduł nadrzędny (root)
- **common** – wspólne DTO, mapery, klasy pomocnicze
- **datagatherer** – pobiera kursy z zewnętrznych API i publikuje zdarzenia
- **dataprovider** – warstwa REST API, uwierzytelnianie, subskrypcje, powiadomienia

---

## ⚙️ Stos technologiczny

- **Java 21 (LTS)**
- **Spring Boot 3.3**
    - Spring Web (REST API)
    - Spring Security (JWT)
    - Spring Data JPA (Hibernate + H2/MySQL)
    - Spring Mail (JavaMailSender)
- **Maven 3.9+**
- **Lombok**
- **JUnit 5** i **Mockito**
- **H2** (testy/dev), **MySQL** (produkcja)

---

## 🚀 Uruchomienie

### Wymagania

- Zainstalowana JDK **21**
- Maven **3.9+**
- (opcjonalnie) serwer SMTP lub Mailtrap do testowania e-maili

### Klonowanie i budowanie
```bash
git clone https://github.com/twoj-login/alarm-walutowy.git
cd alarm-walutowy
mvn clean package
Uruchomienie lokalne
W module dataprovider:


mvn spring-boot:run
Lub uruchom klasę DataProviderApplication w IntelliJ.
API dostępne pod adresem:
👉 http://localhost:8080/api

🔑 Przebieg autoryzacji
Rejestracja
POST /api/auth/register
Przykładowe body:


{
  "username": "jan",
  "password": "tajne123",
  "email": "jan@example.com"
}
Logowanie
POST /api/auth/login
Przykładowe body:


{
  "username": "jan",
  "password": "tajne123"
}
Odpowiedź:


{
  "token": "<jwt-token>",
  "user": { "id": 1, "username": "jan", "email": "jan@example.com" }
}
Token JWT należy przekazywać w nagłówku Authorization:


Authorization: Bearer <jwt-token>
📧 Powiadomienia e-mail
Włączanie/wyłączanie: app.notifications.enabled (true/false)

Nadawca: app.notifications.from

Przykład (application.yml):


app:
  notifications:
    enabled: true
    from: no-reply@alarm-walutowy.local
🧪 Testy
Uruchomienie testów:


mvn test
Testy obejmują:

jednostkowe (AuthService, UserService, NotificationService)

integracyjne (API, rejestracja/logowanie)

📄 Wersja
Aktualne wydanie: 1.4.1

👨‍💻 Autor
Projekt stworzony w ramach bootcampu Java CodersLab
Autor: Gabriel Stremecki