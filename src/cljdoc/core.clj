(ns cljdoc.core)

(defn write [text]
  (spit "dump.yml" (str text "\n") :append true))

(defn break [text]
  (clojure.string/split text #"\n"))

(defn print-vars [n]
  (doseq [x (ns-publics n)]
    (write (str "    " \" (first x) \" ":"))
    (let [m (meta (last x))]
      (when m
        (write (str "      name: " \" (m :name) \"))
        (when (m :doc)
          (write (str "      doc: >"))
          (doseq [l (break (m :doc))]
            (write (str "        " l)))
        (println m))
      ))))

(defn process-namespace-list
  ([list]
    (doseq [x list]
      (write (str (ns-name x) ":"))
      (when (meta x)
        (write (str "  doc: \"" ((meta x) :doc) "\"")))
      (write (str "  ns-name: \"" (ns-name x) "\""))
      (write (str "  vars:"))
      (print-vars (ns-name x)))))

(defn -main []
  (process-namespace-list (all-ns)))