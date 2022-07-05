.PHONY: test

repl:
	docker-compose exec app bash -c "lein repl :start { :host 0.0.0.0 :port 4001 }"

lint:
	lein clj-kondo

test:
	lein test

web:
	docker-compose exec app bash -c "npx shadow-cljs watch reminder"

build-cli:
	lein with-profile cli bin && mv target/.*-SNAPSHOT /usr/local/bin/bot

migrate:
	lein migratus migrate

up:
	docker-compose up

exec:
	docker-compose exec app bash
