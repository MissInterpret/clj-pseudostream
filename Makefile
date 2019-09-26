VERSION:=$(shell git rev-parse --short=10 HEAD)

target:
	mkdir -p target


target/classes/namespaces.class: deps.edn src/**/* target
	clojure -A:build -m package

build: target/classes/namespaces.class

clean:
	rm -rf target
