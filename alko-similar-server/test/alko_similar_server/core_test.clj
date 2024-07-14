(ns alko-similar-server.core-test
  (:require [clojure.test :refer [use-fixtures, deftest, testing, is]]
            [integrant.core :as ig]
            [ring.mock.request :as mock]
            [muuntaja.core :as m]
            [alko-similar-server.scraper :as scraper]))

(defonce test-system (atom nil))

(defn halt []
  (swap! test-system #(when % (ig/halt! %))))

(defn go
  [config-file]
  (halt)
  (reset! test-system (let [config (-> config-file
                                       slurp
                                       ig/read-string)]
                        (scraper/scrape-data)
                        (-> config
                            ig/prep
                            ig/init))))

(defn test-system-fixture-runner [testfunc]
  (try
    (go "dev/resources/config.edn")
    (testfunc)
    (finally
      (halt))))

(use-fixtures :once test-system-fixture-runner)

(defn test-endpoint
  ([uri]
   (test-endpoint uri {}))
  ([uri opts]
   (let [app      (@test-system :alko-similar-server/app)
         response (app (-> (mock/request :get uri)
                           (cond-> (:body opts) (mock/json-body (:body opts)))))]
     (update response :body (partial m/decode "application/json")))))

(deftest health-route-test
  (testing "Health route test"
    (is (= {:status 200 :headers {} :body nil}
           (test-endpoint "/api/health")))
    (is (= 404
           (-> (test-endpoint "wrong-url")
               :status)))))

(deftest get-product-route-test
  (testing "Get product route test"
    (let [id          "447328"
          response    (test-endpoint (str "/api/product/" id))
          status      (:status response)
          received-id (-> response
                          :body
                          :id)]
      (is (= 200 status))
      (is (= received-id id)))))

(deftest get-similar-route-test
  (testing "Get similar route test"
    (let [id       "447328"
          response (test-endpoint (str "/api/similar/" id))
          status   (:status response)
          similar  (-> response
                       :body
                       :similar)]
      (is (= 200 status))
      (is (not (nil? similar)))
      (is (> (count similar) 0)))))

(comment
  (test-endpoint "/api/health")
  (test-endpoint "/wrong-url")
  (test-endpoint "/api/product/447328") 
  (let [id       "447328"
        response (test-endpoint (str "/api/product/" id))
        received-id (-> response
                        :body
                        :id)]
    (println received-id)
  )
  (go "resources/config.edn")
  (halt)
  )
