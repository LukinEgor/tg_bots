FROM clojure:openjdk-8-lein-2.9.8-buster

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY project.clj /usr/src/app/
RUN lein deps

COPY . /usr/src/app

RUN mv "$(lein with-profile api ring uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" server.jar
RUN lein with-profile cli bin && mv target/.*-SNAPSHOT /usr/local/bin/bot

CMD ["java", "-jar", "server.jar"]
