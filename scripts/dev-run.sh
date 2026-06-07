#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/colors.sh"

cd "$SCRIPT_DIR/.."

if [ -f "docker/.env" ]; then
    echo -e "${BLUE}⟳ Loading environment variables from docker/.env...${NC}"
    set -a
    source docker/.env
    set +a
else
    echo -e "${YELLOW}⚠ docker/.env not found. Using system environment variables only.${NC}"
fi

echo -e "${BLUE}▶︎ Starting PostgreSQL container for dev environment...${NC}"
docker-compose -f docker/docker-compose.yml up -d postgres-db

echo -e "${BLUE}▶︎ Running Spring Boot with 'dev' profile...${NC}"
./gradlew bootRun --args='--spring.profiles.active=dev'