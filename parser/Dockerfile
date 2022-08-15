FROM node

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
COPY package*.json ./

RUN npm install
# If you are building your code for production
# RUN npm ci --only=production
RUN npx playwright install-deps

# Bundle app source
COPY . .

EXPOSE 8585
CMD [ "node", "index.js" ]