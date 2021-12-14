(ns desafio01.db(:use clojure.pprint)
  (:require [datomic.api :as d]
            [clojure.walk :as w]))

(def db-uri "datomic:dev://localhost:4334/desafio03")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco []
  (d/delete-database db-uri))

(def schema [
             ;Cliente
             {:db/ident       :cliente/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity
              :db/doc         "uuid do cliente"}
             {:db/ident       :cliente/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O nome do cliente"}
             {:db/ident       :cliente/cpf
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O cpf do cliente"}
             {:db/ident       :cliente/email
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O email do cliente"}
             {:db/ident       :cliente/cartao
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/one
              :db/doc         "Cartao do cliente"}

             ;Cartao
             {:db/ident       :cartao/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity
              :db/doc         "uuid do cartao"}
             {:db/ident       :cartao/numero
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O numero do cartao"}
             {:db/ident       :cartao/cvv
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O cvv do cartao"}
             {:db/ident       :cartao/validade
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one
              :db/doc         "A validade do cartao"}
             {:db/ident       :cartao/limite
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "O limite disponivel do cartao"}
             {:db/ident       :cartao/compras
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many
              :db/doc         "Compras do cartao"}


             ;Compras
             {:db/ident       :compra/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity
              :db/doc         "uuid da compra"}
             {:db/ident      :compra/data
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one
              :db/doc         "Data da compra"}
             {:db/ident      :compra/valor
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "Valor da compra"}
             {:db/ident      :compra/estabelecimento
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Estabelecimento da compra"}
             {:db/ident      :compra/categoria
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Categoria da compra"}])

(defn cria-schema [conn]
  (d/transact conn schema))

;-------------Salvar cliente------------------------
(defn salvar-cliente [conn cliente]
  (d/transact conn [cliente]))

;-------------Salvar compra--------------------------
(defn salvar-compra [conn cartao-id compra]
  (d/transact conn [compra])
  (d/transact conn [[:db/add [:cartao/id cartao-id] :cartao/compras [:compra/id (:compra/id compra)]]]))

;-------------Buscar Compras do cartao---------------------------
(defn remove-ids [entidade]
  (if (map? entidade)
    (dissoc entidade :db/id)
    entidade))

(defn datomic-para-entidade [entidade]
  (w/prewalk remove-ids entidade))

(defn buscar-compras [db cartao-id-solicitado]
  (datomic-para-entidade
    (d/q '[:find [(pull ?compras [*]) ...]
           :in $ ?cartao-id
           :where [?cartao :cartao/id ?cartao-id]
           [?cartao :cartao/compras ?compras]
           ] db cartao-id-solicitado)))



