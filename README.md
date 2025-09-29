# Alarm Walutowy - wersja basic (ale działa)


# Alarm Walutowy

Projekt końcowy — aplikacja do monitorowania kursów walut i wysyłania powiadomień o ich zmianach.

---

## Architektura

System składa się z **dwóch mikroserwisów**:

- **DataGatherer** (port `8081`)
    - pobiera kursy walut z zewnętrznego API (lub z pliku `sample-openexchangerates.json` w trybie mock)
    - wykrywa zmiany powyżej zadanego progu
    - wysyła zdarzenia (JSON) do RabbitMQ

- **DataProvider** (port `8080`)
    - odbiera zdarzenia z RabbitMQ i zapisuje do bazy danych (Postgres)
    - zarządza użytkownikami i subskrypcjami (rejestracja, logowanie, JWT)
    - udostępnia REST API do historii kursów i subskrypcji



## Wymagania

- **Java 17** (lub wyższa)
- **Maven 3.9+**
- **Docker** + **Docker Compose**
- IntelliJ IDEA (rekomendowane)

---

## Uruchomienie środowiska

1. W katalogu głównym projektu uruchom:

   ```bash
   docker compose up -d
Uruchomi to:

Postgres (localhost:5432, baza currencydb, user app, pass app)

RabbitMQ (localhost:5672 oraz UI na http://localhost:15672, login: guest, hasło: guest)

Sprawdź czy kontenery działają:

docker ps
Uruchomienie aplikacji
DataGatherer

cd datagatherer
mvn spring-boot:run

Swagger UI: http://localhost:8081/swagger-ui.html

DataProvider

cd dataprovider
mvn spring-boot:run
Swagger UI: http://localhost:8080/swagger-ui.html

Testy end-to-end


Rejestracja użytkownika
W Swaggerze DataProvider (/api/auth/register) wywołaj:



{
"username": "test",
"password": "test123",
"email": "test@example.com"
}
Logowanie i pobranie tokenu JWT
/api/auth/login → w odpowiedzi otrzymasz token.
Kliknij przycisk Authorize w Swaggerze i wklej:

Bearer <twój_token>

Dodanie subskrypcji
/api/subscriptions (POST):

{
"currency": "EUR",
"thresholdPercent": 0.5
}

##Manualne wywołanie pobierania kursów

W Swaggerze DataGatherer → POST /api/status/poll → 200 OK.

DataGatherer pobierze kursy i opublikuje wiadomość do RabbitMQ.

DataProvider odbierze wiadomość i zapisze do tabeli rate_history.

Sprawdzenie historii kursów
/api/rates/USD/EUR/last50 → powinieneś zobaczyć ostatnie notowania.

Sprawdzenie subskrypcji
/api/subscriptions (GET) → zwróci listę aktywnych subskrypcji.

Struktura katalogów
common/ — klasy współdzielone (np. RateUpdateMessage)

datagatherer/ — serwis pobierający kursy i publikujący zdarzenia

dataprovider/ — serwis użytkowników, subskrypcji, historii i powiadomień

docker-compose.yml — Postgres + RabbitMQ

README.md — dokumentacja projektu

Plan Rozbudowy


Dodanie MailHog do Docker Compose, aby testować powiadomienia e-mail.

Rozszerzenie NotificationService w DataProvider, by wysyłał e-maile do subskrybentów.

Testy integracyjne (Testcontainers).