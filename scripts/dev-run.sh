#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/colors.sh"

cd "$SCRIPT_DIR/.."

echo -e "${BLUE}▶︎ Starting PostgreSQL container for dev environment...${NC}"
docker-compose -f docker/docker-compose.yml up -d postgres-db

echo -e "${BLUE}▶︎ Running Spring Boot with 'dev' profile...${NC}"
./gradlew bootRun --args='--spring.profiles.active=dev'