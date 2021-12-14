(ns desafio01.model
  (:require [schema.core :as s]))

(defn uuid [] (java.util.UUID/randomUUID))

(s/def PosNum (s/pred #(or (pos? %) (zero? %))))

(s/def Compra {:data s/Str, :valor PosNum, :estabelecimento s/Str, :categoria s/Str })

(s/defn nova-compra :- Compra
  [data valor estabelecimento categoria]
  {:compra/id              (uuid)
   :compra/data            data
   :compra/valor           valor
   :compra/estabelecimento estabelecimento
   :compra/categoria       categoria})

(s/def Cartao {:numero s/Str, :cvv s/Str, :validade s/Str, :limite s/Num, :compras [Compra]})

(s/defn novo-cartao :- Cartao
  [numero cvv validade limite compras]
  {:cartao/id                       (uuid)
   :cartao/numero                   numero
   :cartao/cvv                      cvv
   :cartao/validade                 validade
   :cartao/limite                   limite
   (s/optional-key :cartao/compras) compras
   })

(s/def Cliente {:cliente/nome s/Str, :cliente/cpf s/Str, :cliente/email s/Str, (s/optional-key :cliente/cartao) Cartao})

(s/defn novo-cliente  [nome cpf email cartao]
  {:cliente/id     (uuid)
   :cliente/nome   nome
   :cliente/cpf    cpf
   :cliente/email  email
   :cliente/cartao cartao
   })

(s/def ResumoCategoria {:categoria s/Str, :valor s/Num})

