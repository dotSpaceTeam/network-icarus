# Dev Quick Setup (Docker)

---

1. Enable Dev mode in `src/resources/application.properties` named `node.dev.mode`.
2. Start docker postgres:
   ```
   docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=test -d postgres
   ```
3. Rabbitmq:
   ```
   docker run -d --hostname my-rabbit --name rabbitmq -e RABBITMQ_DEFAULT_VHOST=icarus -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password -p 15672:15672 -p 5672:5672 rabbitmq:3-management
   ```
4. Run Driver class

---