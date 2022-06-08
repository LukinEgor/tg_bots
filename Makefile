.PHONY: test

repl:
	lein repl :start { :host 0.0.0.0 :port 4001 }

test:
	DB_NAME=rss_test lein test

web:
	lein figwheel

build-cli:
	lein with-profile cli bin && mv target/*-SNAPSHOT /usr/local/bin/bot
