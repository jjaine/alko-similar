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

RUN --mount=type=secret,id=jdbc_url \
    export JDBC_DATABASE_URL=$(cat /run/secrets/jdbc_url)

CMD JDBC_DATABASE_URL=$JDBC_DATABASE_URL lein run "resources/config.edn"