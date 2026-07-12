#!/usr/bin/env bash
set -euo pipefail

source ./scripts/colors.sh

echo -e "${RED}⟳ Rebuilding images (no cache)...${NC}"
docker-compose -f ./docker/docker-compose.yml build --no-cache
./scripts/docker-up.sh
echo -e "${GREEN}✔︎ Rebuild complete and containers are running!${NC}"