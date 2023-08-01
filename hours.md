# Log

| date      | hours | description  |
| :--------:|:------| :-----|
| 03.12.2022 | 3     | initial setup, data scraping |
| 04.12.2022 | 3     | building basic API |
| 05.12.2022 | 3     | basic API server working, logic todo |
| 27.01.2023 | 8     | rewrite for cleaner structure, add initial Dockerfile |
| 28.01.2023 | 2     | setup swagger, find cause for 500 error |
| 29.01.2023 | 4     | fix Dockerfile, add docker-compose.yml, implement API for product details |
| 31.01.2023 | 4     | fix formatting, support for getting similar products based on description, images for products |
| 08.02.2023 | 4     | create first tests, make tests work with lein test for pipeline usage |
|            | 1     | add pipeline for test & deploy |
|            | 1     | tried to deploy image to fly.io, encountered limits |
|            | 1     | pipeline to build docker image and publish it to hub |
| 15.02.2023 | 7     | figure out deployment to cloud instance, add similar products with filtering to product request |
| 16.02.2023 | 1     | add scheduled tasks to pipeline, update readme for server |
|            | 1     | initial setup for client |
| 17.02.2023 | 8     | client initial version with working request etc. |
| 19.02.2023 | 2     | add Dockerfile for client, pipeline for building |
| 02.03.2023 | 5     | setup development env with docker, small fixes |
| 05.03.2023 | 8     | styles for product page |
| 06.03.2023 | 1     | change dockerfiles to use multi-stage builds for different platforms, add label info to product view |
|            | 1     | add more product info, separate views into different files |
|            | 3     | show similar products |
| 07.03.2023 | 6     | implement filtering |
|            | 3     | separate similars query in API, make price slider filter work, style slider |
| 08.03.2023 | 6     | styling the front page, implement barcode scanning & searching |
| 09.03.2023 | 9     | setup nginx as reverse proxy with https support |
| 10.03.2023 | 11    | integrate postgres as a database for the project, setup for both dev and prod |
| 11.03.2023 | 5     | show recently searched and popular products on front page, add hovers |
| 12.03.2023 | 1     | fix bug with postgres env variable in production |
| 19.04.2023 | 2     | fix scraping |
| 26.04.2023 | 1     | actually fix scraping |
| 12.06.2023 | 3     | debug database problem, add logging |
| 01.08.2023 | 6     | scraping is broken, make workaround for updating xlsx manually, fix bugs with unknown products, add better logging for scraping |
| total      | 124   | | 