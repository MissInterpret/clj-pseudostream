(ns clj-pseudostream.spec.core
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))


(create-ns 'stedi.tx-event-kit)
(alias 'tx-event-kit 'stedi.tx-event-kit)

(create-ns 'stedi.tx-event-kit.event)
(alias 'event 'stedi.tx-event-kit.event)

(s/def ::event/type #{"purchase-order"
                      "purchase-order-acknowledgment"
                      "asn"
                      "invoice"})
(s/def ::event/action #{"created"})
(s/def ::event/uuid-str (s/spec (s/and string?
                                       #(try
                                          (UUID/fromString %)
                                          true
                                          (catch Exception _
                                            false)))
                                :gen #(gen/bind
                                        (s/gen uuid?)
                                        (fn [uuid] (gen/return (str uuid))))))
(s/def ::event/org-id ::event/uuid-str)
(s/def ::event/ident ::event/uuid-str)
(s/def ::event/not-empty-str (s/and string? not-empty))
(s/def ::event/realm ::event/not-empty-str)
(s/def ::event/source ::event/realm)

(s/def ::event/detail
  (s/keys :req-un [::event/type
                   ::event/action
                   ::event/org-id
                   ::event/ident
                   ::event/realm]))

(s/def ::tx-event-kit/event
  (s/keys :req-un [::event/type
                   ::event/source
                   ::event/detail]))



(defprotocol Handler
  "Defines the interface through which all invocations occur."
  (-handle [handler event]))

(defrecord DefaultHandler [tx-svc-client pp-api-client]
  Handler
  (-handle [handler event]
    (->> (tx-svc/fetch tx-svc-client event)
         (sales-order/po->sales-order)
         (pp-client/post pp-api-client))))

(defn null-handler
  "Returns a no-op handler for generative testing."
  []
  (reify Handler
    (-handle [handler event])))

(defn- pp-api-token
  "Returns the API token used to authenticate against the Premier Performance
  API."
  []
  (let [domain (config/get-env "AWS_STACK_NAME")
        path   (format "/%s/pp-api-token" domain)]
    (config/get-param path)))

(defn default-handler
  "Constructs a DefaultHandler with the default clients."
  []
  (map->DefaultHandler
    {:tx-svc-client (tx-svc/default-client)
     :pp-client     (pp-client/default-client (pp-api-token))}))

(defn handle
  "Handles `event` by fetching the associated purchase order, transforming it, and
  sending it to the Premier Performance API."
  ([event]
   (handle event (default-handler)))
  ([event handler]
   (-handle handler event)))



(s/def :stedi.premier-performance/handler
  (s/spec any?
          :gen #(gen/return (handler/null-handler))))

(s/def :ok/status #{200 201})

(s/def ::ok (s/keys :req-un [:ok/status]))

(s/fdef stedi.premier-performance.handler/handle
  :args (s/cat :event :stedi.tx-event-kit/event
               :handler :stedi.premier-performance/handler)
  :ret ::ok)
