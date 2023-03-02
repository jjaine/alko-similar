# Base image that includes the JDK
FROM clojure:lein

# Install Node.js & npm
RUN apt-get update && apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_16.x | bash -
RUN apt-get install -y nodejs

RUN mkdir -p /app
WORKDIR /app

# Add sources
COPY . /app

# Install dependencies
RUN npm install

# Run the app in development mode
CMD npm run dev
