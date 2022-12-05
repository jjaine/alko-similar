(ns alko-similar-server.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [alko-similar-server.server :refer :all]
            [mount.core :as mount]))

(defn mount [f]
  (mount/start)
  (f)
  (mount/stop))

(use-fixtures :once mount)

(def app (build-app))

(deftest health-route-test
  (testing "Health route test"
    (is (= {:status 200, :body "ok"}
           (app {:request-method :get, :uri "/health"})))
    (is (= {:status 404, :body "", :headers {}}
           (app {:request-method :get, :uri "wrong-url"})))))

(deftest get-similar-route-test
  (testing "Get similar route test"
    (let [id       "915083"
          response (app {:request-method :get
                         :uri            "/api/get-similar"
                         :query-params   {"id" id}})
          status   (:status response)
          body     (slurp (:body response))]
      (is (= 200 status))
      (is (s/includes? body id)))))
