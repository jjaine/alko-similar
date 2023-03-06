# Base image that includes the JDK
FROM --platform=$BUILDPLATFORM clojure:lein AS builder

# Install Node.js & npm
RUN apt-get update && apt-get install -y curl \
 && curl -sL https://deb.nodesource.com/setup_16.x | bash - \
 && apt-get install -y nodejs

RUN mkdir -p /app
WORKDIR /app

# Add sources
COPY . /app

# Install dependencies
RUN npm install

# Build css
RUN npx tailwindcss build src/css/app.css -o public/app.css

# Build the app
RUN npm run release

FROM node:16-alpine
COPY --from=builder /app/public /app/public
CMD npx serve public -p 3001