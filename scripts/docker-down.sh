#!/usr/bin/env bash
set -euo pipefail

source ./scripts/colors.sh

echo -e "${BLUE}⏸︎ Stopping containers...${NC}"
docker-compose -f ./docker/docker-compose.yml down
echo -e "${GREEN}✔︎ Containers stopped!${NC}"