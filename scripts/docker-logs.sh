#!/usr/bin/env bash
set -euo pipefail

source ./scripts/colors.sh

echo -e "${CYAN}☰ Showing Spring Boot logs... Press Ctrl+C to exit${NC}"
docker-compose -f ./docker/docker-compose.yml logs -f spring-jwt-app