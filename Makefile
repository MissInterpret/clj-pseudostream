target-dir             := target

.PHONY: aot

aot:
	mkdir -p $(target-dir) && mkdir -p classes
	clojure -e "(compile,'clj-psuedostream.compile)"
	if [ -d $(target-dir)/classes ]; then rm -r $(target-dir)/classes; fi
	mv classes $(target-dir)
