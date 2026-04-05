source ./colors.sh

echo -e "${YELLOW}🔄 Rebuilding images (no cache)...${NC}"
docker-compose -f ../docker/docker-compose.yml build --no-cache
./docker-up.sh
echo -e "${GREEN}✅ Rebuild complete and containers are running!${NC}"