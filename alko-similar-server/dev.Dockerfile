# Base image that includes the Clojure CLI tools
FROM clojure:lein

RUN mkdir -p /app
WORKDIR /app

# Prepare deps
COPY project.clj /app
RUN lein deps

ENV PORT 3000

CMD LEIN_REPL_HOST=0.0.0.0 JDBC_DATABASE_URL=$JDBC_DATABASE_URL lein update-in '[:repl-options,:nrepl-middleware]' conj '["cider.nrepl/cider-middleware"]' -- with-profile +dev repl :headless :port 40000