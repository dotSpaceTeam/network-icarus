# Dev Quick Setup (Docker)

---

1. Enable Dev mode in `src/resources/application.properties` named `node.dev.mode`.
2. Start docker postgres:
   ```
   docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=test -d postgres
   ```
3. Run Driver class

---