# Base image that includes the Clojure CLI tools
FROM clojure:lein

RUN mkdir -p /app
WORKDIR /app

# Prepare deps
COPY project.clj /app
RUN lein deps

# Add sources
COPY . /app

CMD lein run "resources/config.edn"