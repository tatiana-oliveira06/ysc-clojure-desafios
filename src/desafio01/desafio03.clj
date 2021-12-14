(ns desafio01.desafio03
  (:use clojure.pprint)
  (:require [desafio01.db :as db]
            [desafio01.model :as model]
            [datomic.api :as d]
            [schema.core :as s]
            [java-time :as j]))

(db/apaga-banco)

(def conn (db/abre-conexao))

(db/cria-schema conn)

(def data (j/sql-timestamp (j/local-date-time)))

(def compra01 (model/nova-compra data 200.00M "Extra super SA" "Alimentação"))
(def compra02 (model/nova-compra data 545.00M "Drogasil" "Saúde"))
(def compra03 (model/nova-compra data 21.30M "Drograsil" "Saúde"))
(def compra04 (model/nova-compra data 78.67M "Restaurante X" "Alimentação"))
(def compra05 (model/nova-compra data 567.99M "Wise up" "Educação"))
(def cartao (model/novo-cartao "5647 7858 7689 6573" "234" data 2000M [compra01,compra02,compra03,compra04]))
(def cliente (model/novo-cliente "Joao da Silva" "89745378932" "joao@gmail.com" cartao))


(s/defn salvar-cliente [cliente-param]
    (db/salvar-cliente conn cliente-param))

(s/defn salvar-compra [conn cartao-id compra ]
  (db/salvar-compra conn cartao-id compra))

(s/defn buscar-compras [db cartao-id]
  (db/buscar-compras db cartao-id))


(pprint (salvar-cliente cliente))

(pprint (buscar-compras (d/db conn) (:cartao/id cartao)))

(pprint (salvar-compra conn (:cartao/id cartao) compra05))

(pprint (buscar-compras (d/db conn) (:cartao/id cartao)))


