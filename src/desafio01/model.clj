(ns desafio01.model
  (:require [schema.core :as s]))

(s/def PosNum (s/pred #(or (pos? %) (zero? %))))

(s/def Compra {:data s/Str, :valor PosNum, :estabelecimento s/Str, :categoria s/Str })

(s/def Cartao {:numero s/Str, :cvv s/Str, :validade s/Str, :limite s/Num, :compras [Compra]})

(s/def Cliente {:nome s/Str, :cpf s/Str, :email s/Str, :cartao Cartao})

(s/def ResumoCategoria {:categoria s/Str, :valor s/Num})

;(println (s/validate Compra
;                     {:data            "01/11/2021"
;                      :valor            200.00
;                      :estabelecimento "Extra super SA"
;                      :categoria       "Alimentação"}))

;(println (s/validate Cartao {:numero  "1324567897643256" :cvv "123" :validade "03/2030" :limite 2000.00
;                             :compras [{:data "01/11/2021" :valor 200.00 :estabelecimento "Extra super SA" :categoria "Alimentação"}
;                                       {:data "23/09/2020" :valor 545.00 :estabelecimento "Extra super SA" :categoria "Saúde"}
;                                       {:data "03/04/2021" :valor 78.67 :estabelecimento "Extra super SA" :categoria "Alimentação"}
;                                       {:data "15/01/2020" :valor 21.30 :estabelecimento "Extra super SA" :categoria "Saúde"}
;                                       {:data "12/11/2021" :valor 567.99 :estabelecimento "Extra super SA" :categoria "Educação"}]}))
;
;(println (s/validate Cliente { :nome "Maria da Silva" :cpf "12356743565" :email "maria@gmal.com"
;                             :cartao     {:numero  "1324567897643256" :cvv "123" :validade "03/2030" :limite 2000.00
;                                          :compras [{:data "01/11/2021" :valor 200.00 :estabelecimento "Extra super SA" :categoria "Alimentação"}
;                                                    {:data "23/09/2020" :valor 545.00 :estabelecimento "Extra super SA" :categoria "Saúde"}
;                                                    {:data "03/04/2021" :valor 78.67 :estabelecimento "Extra super SA" :categoria "Alimentação"}
;                                                    {:data "15/01/2020" :valor 21.30 :estabelecimento "Extra super SA" :categoria "Saúde"}
;                                                    {:data "12/11/2021" :valor 567.99 :estabelecimento "Extra super SA" :categoria "Educação"}]}}))