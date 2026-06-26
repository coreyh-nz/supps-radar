# Local Development

This directory contains resources used for local development only.

## Prerequisites

- Docker Desktop (or Docker Engine + Docker Compose)

## Starting the development environment

```bash
docker compose up -d
```

This will start:

- PostgreSQL

## Stopping the environment

```bash
docker compose down
```

To also remove volumes (this will delete all local data):

```bash
docker compose down -v
```

## Services

| Service    | URL              |
|------------|------------------|
| PostgreSQL | `localhost:5432` |
