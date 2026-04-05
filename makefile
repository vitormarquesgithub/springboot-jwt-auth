.PHONY: help up down logs reset rebuild dev

help:
	@echo "Available commands:"
	@echo "  make up        - Start Docker containers (PostgreSQL + App)"
	@echo "  make down      - Stop Docker containers"
	@echo "  make logs      - Show Spring Boot logs (follow mode)"
	@echo "  make reset     - Full reset (down + up)"
	@echo "  make rebuild   - Rebuild images without cache and start"
	@echo "  make dev       - Run app locally with 'dev' profile"

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

dev:
	bash ./scripts/dev-run.sh