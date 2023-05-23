(ns models.core)

(defmacro defmodel
  [name fields]
  (list 'do
      (list 'defrecord name fields)
      (list 'defn (symbol (str "create-" name)) '[] (list 'print "test"))))

