(ns desafio01.desafio02
  (:use clojure.pprint)
  (:require [java-time :as time]
            [schema.core :as s]
            [desafio01.model :as d.model]))

(def cliente01 {:nome "Maria da Silva" :cpf "12356743565" :email "maria@gmal.com"
                :cartao     {:numero  "1324567897643256" :cvv "123" :validade "03/2030" :limite 2000.00
                             :compras [{:data "01/11/2021" :valor 200.00 :estabelecimento "Extra super SA" :categoria "Alimentação"}
                                       {:data "23/09/2020" :valor 545.00 :estabelecimento "Extra super SA" :categoria "Saúde"}
                                       {:data "03/04/2021" :valor 78.67 :estabelecimento "Extra super SA" :categoria "Alimentação"}
                                       {:data "15/01/2020" :valor 21.30 :estabelecimento "Extra super SA" :categoria "Saúde"}
                                       {:data "12/11/2021" :valor 567.99 :estabelecimento "Extra super SA" :categoria "Educação"}]}})

;OBTER COMPRAS

(defn obter-compras [cliente]
  (-> cliente
      :cartao
      :compras))


; AGRUPAMENTO POR CATEGORIA
(defn calcula-total-da-categoria [[categoria compras]]
  (let [valor-total-da-categoria (reduce + (map :valor compras))]
    [categoria valor-total-da-categoria]))

(defn calcula-gastos-totais-por-categoria
  [compras]
  (map calcula-total-da-categoria compras))

(defn agrupa-por-categoria [cliente]
  (->> (obter-compras cliente)
       (group-by :categoria)
       (calcula-gastos-totais-por-categoria)
       (pprint)))

;(agrupa-por-categoria cliente01)

;FATURA DO MES
(defn mes-desejado? [compra periodo]
  (let [mes-corrente periodo
        mes-da-fatura (time/format "MM/yyyy" (time/local-date "dd/MM/yyyy" (:data compra)))]
    (= mes-corrente mes-da-fatura))
  )

(defn calcula-total-do-mes [compras periodo]
  (reduce + (map :valor (filter #(mes-desejado? % periodo) compras))))

(defn calcula-valor-da-fatura [cliente periodo]
  (-> (obter-compras cliente)
      (calcula-total-do-mes periodo)
      (pprint)))

;(calcula-valor-da-fatura cliente01 "04/2021")

;LISTAR COMPRAS PELO ESTABELECIMENTO OU VALOR

(s/defn corresponde? :- s/Bool
  [compra :- d.model/Compra, filtro]
  (let [estabelecimento (:estabelecimento compra)
        valor (:valor compra)]
    (if (string? filtro)
      (= estabelecimento filtro)
      (>= valor filtro))))


(s/defn listar-compras :- [d.model/Compra]
  [cliente :- d.model/Cliente, filtro]
  (let [compras (obter-compras cliente)]
    (filter #(corresponde? % filtro) compras)))

(pprint (listar-compras cliente01 545M))










