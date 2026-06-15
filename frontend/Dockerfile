FROM node:20-alpine
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 5173

# Run vite with host parameter so it accepts external connections
CMD ["npm", "run", "dev", "--", "--host"]
