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

(def cliente01 {:nome "Maria da Silva" :cpf "12356743565" :email "maria@gmal.com"
              :cartao {:numero  "1324567897643256" :cvv "123" :validade "03/2030" :limite 2000.00
                       :compras [{:data "01/11/2021" :valor 200.00 :estabelecimento "Extra" :categoria "Alimentação"}
                                 {:data "23/09/2020" :valor 545.00 :estabelecimento "Hospital Sao luiz" :categoria "Saúde"}
                                 {:data "03/04/2021" :valor 78.67 :estabelecimento "Extra" :categoria "Alimentação"}
                                 {:data "15/01/2020" :valor 21.30 :estabelecimento "Drograsil" :categoria "Saúde"}
                                 {:data "12/11/2021" :valor 567.99 :estabelecimento "Wise up" :categoria "Educação"}]}})


(s/defn mes-desejado? :- s/Bool
  [compra :- d.model/Compra, periodo :- s/Str]
  (let [mes-corrente periodo
        mes-da-fatura (time/format "MM/yyyy" (time/local-date "dd/MM/yyyy" (:data compra)))]
    (= mes-corrente mes-da-fatura)))

(s/defn calcula-valor-da-fatura :- s/Num
  [cliente :- d.model/Cliente, periodo :- s/Str]
  (let [compras (obter-compras cliente)]
        (reduce + (map :valor (filter #(mes-desejado? % periodo) compras)))))

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


