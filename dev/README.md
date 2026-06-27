# Local Development

This directory contains resources used for local development only.

## Prerequisites

* Docker Desktop (or Docker Engine with Docker Compose)

## Starting the development environment

Start all services in the background:

```bash
docker compose up -d
```

The following services will be started:

* **PostgreSQL** - Primary application database
* **Kafka** - Event streaming platform
* **Kafka UI** - Web interface for inspecting Kafka topics, messages and consumer groups

## Stopping the development environment

Stop all services:

```bash
docker compose down
```

To also remove all persisted data (this will reset PostgreSQL and Kafka):

```bash
docker compose down -v
```

## Available Services

| Service          | Address                 | Notes                                                |
|------------------|-------------------------|------------------------------------------------------|
| PostgreSQL       | `localhost:5432`        | Database                                             |
| Kafka (Internal) | `kafka:9092`            | Used by Docker containers                            |
| Kafka (External) | `localhost:9094`        | Used by applications running on your host machine    |
| Kafka UI         | `http://localhost:8080` | Web interface for browsing Kafka topics and messages |
