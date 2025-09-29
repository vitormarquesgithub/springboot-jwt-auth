source ./colors.sh

echo -e "${RED}🗑️ Resetting database and containers...${NC}"
./docker-down.sh
./docker-up.sh
echo -e "${GREEN}✅ Database reset and containers are running!${NC}"