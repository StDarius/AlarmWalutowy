README – AlarmWalutowy v1.4
🎯 Opis projektu

AlarmWalutowy to aplikacja, której celem jest monitorowanie kursów walut i powiadamianie użytkowników, gdy zmiana przekroczy zdefiniowany próg procentowy.
Projekt łączy się z zewnętrznym API kursów walut, umożliwia zakładanie kont, subskrypcję wybranych par walutowych oraz wysyła powiadomienia e-mail.

Od wersji 1.4 dodano role użytkowników (ROLE_USER, ROLE_ADMIN), co wprowadza prostą warstwę autoryzacji – np. różne poziomy dostępu do endpointów.

🛠 Technologie

Java 21, Spring Boot 3.3

Spring Security + JWT

Spring Data JPA + H2 (dev), PostgreSQL (prod)

Flyway (zarządzanie schematem bazy)

MailHog (powiadomienia e-mail w dev)

Lombok (redukcja boilerplate)

Docker (opcjonalnie)

🚀 Funkcjonalności

Rejestracja i logowanie z JWT, walidacja danych

Subskrypcja wybranych walut z progami alertów

Powiadomienia mailowe (MailHog w dev)

Integracja z zewnętrznym API kursów walut

Obsługa ról użytkowników:

ROLE_USER – dostęp do standardowych funkcji aplikacji

ROLE_ADMIN – potencjalny dostęp do funkcji administracyjnych

📊 Priorytety funkcjonalności (MoSCoW) + Estymaty
Kategoria	Funkcjonalność	Estymata (h)	Status v1.4
M	Rejestracja i logowanie z JWT	8	✅ Gotowe
M	Subskrypcje walut z progami alertów	12	✅ Gotowe
M	Powiadomienia e-mail (MailHog w dev)	6	✅ Gotowe
M	Integracja z zewnętrznym API (kursy walut)	10	✅ Gotowe
M	Role użytkowników (USER/ADMIN)	8	✅ Gotowe
S	Historia kursów walut + DTO	8	✅ Gotowe
S	Panel statusu API (/api/status)	3	✅ Gotowe
C	Dashboard w React/Thymeleaf	16	❌ Jeszcze nie
W	Integracja z Google OAuth	-	❌ Nie w tej wersji
W	Deploy na VPS z CI/CD	-	❌ Nie w tej wersji
🔑 Zmiany w wersji 1.4

Dodano obsługę ról użytkowników (ROLE_USER, ROLE_ADMIN) w modelu i serwisach.

Rozszerzono logikę rejestracji o przypisywanie domyślnej roli.

Zaktualizowano dokumentację README o sekcję MoSCoW + Estymaty.

📦 Uruchamianie
Profil deweloperski

Wymagane: Java 21, Maven, opcjonalnie Docker + MailHog

Komenda:

mvn spring-boot:run -pl dataprovider


Aplikacja dostępna pod adresem: http://localhost:8080

Profil produkcyjny

Wymagane: PostgreSQL + RabbitMQ

Konfiguracja w application.yml (prod).