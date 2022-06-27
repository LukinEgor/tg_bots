FROM clojure:openjdk-8-lein-2.9.8-buster

RUN apt update -y && apt-get install -y nodejs npm

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY project.clj /usr/src/app/
RUN lein deps

COPY package.json /usr/src/app/
COPY package-lock.json /usr/src/app/
RUN npm install

COPY . /usr/src/app

RUN npx shadow-cljs release reminder
RUN mv "$(lein ring uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" server.jar
RUN lein with-profile cli bin && mv target/.*-SNAPSHOT /usr/local/bin/bot

CMD ["java", "-jar", "server.jar"]
