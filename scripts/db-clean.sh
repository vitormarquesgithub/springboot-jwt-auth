#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/colors.sh"

cd "$SCRIPT_DIR/.."

echo -e "${YELLOW}⚠  WARNING: This will DELETE ALL DATABASE DATA!${NC}"
read -p "Are you sure? Type 'yes' to continue: " -r
if [[ ! $REPLY == "yes" ]]; then
    echo -e "${YELLOW}Operation cancelled.${NC}"
    exit 0
fi

echo -e "${RED}⏸︎  Stopping containers and removing database volume...${NC}"
docker-compose -f docker/docker-compose.yml down -v

echo -e "${BLUE}🗘 Starting fresh PostgreSQL container...${NC}"
docker-compose -f docker/docker-compose.yml up -d postgres-db

echo -e "${GREEN}✔︎ Database cleaned! Volume removed and new empty database created.${NC}"
