# Alko similar recommender

Running at [alko-similar.dy.fi](http://alko-similar.dy.fi).

Made as the project for the Fullstack Open course. Log of hours used in [hours.md](hours.md).

## Server
Clojure server that provides API endpoints for server health check and to fetch information about similar products.

### API endpoints
`/api/product/{product-id}` Get product details with id.

**Parameters**

| Name | Description | Type | Example |
|------|-------------|------|---------|
| product-id | Product id in Alko's catalog | string (path) | [000132](https://www.alko.fi/tuotteet/000132) |
| filter-by  | Filter similar products by parameters<br>(package-size, type, subtype, country, package-type) | string (query) | "country,type" |
| min-price  | | float (query) | 10.0 |
| min-price  | | float (query) | 40.0 |

`/scrape` Scrape the alko website to update product database.

`/health` Check if the server running.

## Client
TODO
