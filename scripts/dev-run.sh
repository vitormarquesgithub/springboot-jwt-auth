#!/usr/bin/env bash
set -euo pipefail

source ./scripts/colors.sh

echo -e "${BLUE}▶︎ Running Spring Boot with 'dev' profile...${NC}"
./gradlew bootRun --args='--spring.profiles.active=dev'
