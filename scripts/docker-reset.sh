#!/usr/bin/env bash
set -euo pipefail

source ./scripts/colors.sh

echo -e "${YELLOW}⟳ Resetting database and containers...${NC}"
./scripts/docker-down.sh
./scripts/docker-up.sh
echo -e "${GREEN}✔︎ Database reset and containers are running!${NC}"