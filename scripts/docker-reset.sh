#!/usr/bin/env bash
set -euo pipefail

source ./scripts/colors.sh

echo -e "${YELLOW}⟳ Resetting containers...${NC}"
./scripts/docker-down.sh
./scripts/docker-up.sh
echo -e "${GREEN}✔︎ Reset containers are running!${NC}"