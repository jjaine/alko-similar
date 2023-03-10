# Alko similar recommender

The app recommends similar products from the Alko catalogue.

Running at [alko-similar.dy.fi](https://alko-similar.dy.fi).

Made as the project for the Fullstack Open course. Log of hours used in [hours.md](hours.md).

# Server
Clojure server that provides API endpoints for server health check and to fetch information about similar products.

### API endpoints
`/api/product/{product-id}` Get product details with id.

**Parameters**

| Name | Description | Type | Example |
|------|-------------|------|---------|
| product-id | Product id in Alko's catalog | string (path) | [000132](https://www.alko.fi/tuotteet/000132) |

`/api/similar/{product-id}` Get similar products to product with id.

**Parameters**

| Name | Description | Type | Example |
|------|-------------|------|---------|
| product-id | Product id in Alko's catalog | string (path) | [000132](https://www.alko.fi/tuotteet/000132) |
| filter-by  | Filter similar products by parameters<br>(package-size, type, subtype, country, package-type) | string (query) | "country,type" |
| min-price  | | float (query) | 10.0 |
| min-price  | | float (query) | 40.0 |

`/api/scrape` Scrape the alko website to update product database.

`/api/health` Check if the server running.

# Client
ClojureScript client that provides the users possibility to search for similar products to other Alko products either by id, URL or by using the camera to scan the product's EAN code.

# Development
Use `docker compose -f docker-compose.dev.yml up --build` to build newest versions of the containers and run the development environment.

Connect to the server REPL running at `localhost:40000` and use the `(reset)` in `user.clj` to start the server. You can use VS Code with [Calva](https://calva.io) for example! Then use `(scrape-data)` in `scraper.clj` to scrape the most up-to-date data to the server. The API can be viewed and tested at `http://localhost:3000/docs/`.

The client is running at `http://localhost:3001` in development mode.
