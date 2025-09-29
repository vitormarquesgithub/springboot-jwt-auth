source ./colors.sh

echo -e "${RED}🛑 Stopping containers...${NC}"
docker-compose -f ../docker/docker-compose.yml down
echo -e "${GREEN}✅ Containers stopped!${NC}"