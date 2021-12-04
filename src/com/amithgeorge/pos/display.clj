(ns com.amithgeorge.pos.display)

(def ^:dynamic *device*)

(defmulti show (fn ([type & args] type)))

(defmethod show :price [_ {:keys [price]}]
  (println "inside show price")
  (*device* price))

(defmethod show :invalid [& _]
  (*device* "Invalid code!"))

(defmethod show :not-found [& _]
  (*device* "Not found!"))