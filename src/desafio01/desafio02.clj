(ns desafio01.desafio02
  (:use clojure.pprint)
  (:require [java-time :as time]
            [schema.core :as s]
            [desafio01.model :as d.model]))

(s/set-fn-validation! true)

;OBTER COMPRAS
(defn obter-compras [cliente]
  (-> cliente
      :cartao
      :compras))


; TOTAIS POR CATEGORIA
(s/defn calcula-total-por-categoria :- d.model/ResumoCategoria
  [[categoria compras]]
  (let [valor-total (reduce + (map :valor compras))]
    {:categoria categoria :valor valor-total}))

(s/defn calcula-totais-por-categoria :- [d.model/ResumoCategoria]
  [cliente :- d.model/Cliente]
  (let [compras (obter-compras cliente)
        compras-por-categoria (group-by :categoria compras)]
    (map calcula-total-por-categoria compras-por-categoria)))


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


;ADICIONAR COMPRA

(s/defn compra-repetida?
  [compra :- d.model/Compra, cliente :- d.model/Cliente]
  (some #(= compra %) (obter-compras cliente)))

(s/defn adiciona-compra :- d.model/Cliente
  [cliente :- d.model/Cliente, compra :- d.model/Compra]
  (if (compra-repetida? compra cliente)
    cliente
    (update-in cliente [:cartao :compras] conj compra)))


