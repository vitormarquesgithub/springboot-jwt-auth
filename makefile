.PHONY: help up down logs reset rebuild dev-run dev-stop db-clean

help:
	@echo "Available commands:"
	@echo "  make up         - Start Docker containers (PostgreSQL + App)"
	@echo "  make down       - Stop Docker containers"
	@echo "  make logs       - Show Spring Boot logs (follow mode)"
	@echo "  make reset      - Full reset (down + up)"
	@echo "  make rebuild    - Rebuild images without cache and start"
	@echo "  make dev-run    - Run app locally with 'dev' profile (requires DB container)"
	@echo "  make dev-stop   - Stop development containers (PostgreSQL + Redis)"
	@echo "  make db-clean   - Wipe database volume and recreate (ALL DATA LOST)"

up:
	bash ./scripts/docker-up.sh

down:
	bash ./scripts/docker-down.sh

logs:
	bash ./scripts/docker-logs.sh

reset:
	bash ./scripts/docker-reset.sh

rebuild:
	bash ./scripts/docker-rebuild.sh

dev-run:
	bash ./scripts/dev-run.sh

dev-stop:
	bash ./scripts/dev-stop.sh

db-clean:
	bash ./scripts/db-clean.sh