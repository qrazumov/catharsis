FROM node

WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
# If you are building your code for production
# RUN npm ci --only=production
RUN npx playwright install-deps
COPY . .
EXPOSE 8585
CMD [ "node", "index.js" ]