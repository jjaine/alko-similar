# Base image that includes the Clojure CLI tools
FROM --platform=$BUILDPLATFORM clojure:lein AS builder

RUN mkdir -p /app
WORKDIR /app

# Prepare deps
COPY project.clj /app
RUN lein deps

# Add sources
COPY . /app

FROM clojure:lein
COPY --from=builder /app /app
WORKDIR /app

ENV PORT 3000

CMD --mount=type=secret,id=jdbc_url \
    JDBC_DATABASE_URL=$(cat /run/secrets/jdbc_url) lein run "resources/config.edn"