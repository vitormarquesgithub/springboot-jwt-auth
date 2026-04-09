#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/colors.sh"

cd "$SCRIPT_DIR/.."

echo -e "${RED}⏸︎ Stopping development containers (PostgreSQL and Redis)...${NC}"
docker-compose -f docker/docker-compose.yml stop postgres-db redis
echo -e "${GREEN}✔︎ Development containers stopped.${NC}"