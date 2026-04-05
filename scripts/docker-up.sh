source ./colors.sh

echo -e "${BLUE}🚀 Starting containers...${NC}"
docker-compose -f ../docker/docker-compose.yml up -d
echo -e "${GREEN}✅ Containers are up!${NC}"
echo -e "${BLUE}Spring Boot: http://localhost:8080${NC}"