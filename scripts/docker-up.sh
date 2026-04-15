#!/usr/bin/env bash
set -euo pipefail

source ./scripts/colors.sh

echo -e "${BLUE}▶︎ Starting containers...${NC}"
docker-compose -f ./docker/docker-compose.yml up -d
echo -e "${GREEN}✔︎ Containers are up!${NC}"
echo -e "${BLUE}JWT Auth API: http://localhost:8080/swagger-ui/index.html${NC}"